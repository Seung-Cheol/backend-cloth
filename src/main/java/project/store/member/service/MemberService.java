package project.store.member.service;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.store.common.util.EmailUtil;
import project.store.common.util.EncryptUtil;
import project.store.common.util.RedisUtil;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.api.dto.request.LoginRequestDto;
import project.store.member.api.dto.request.UpdateMyPageRequestDto;
import project.store.member.api.dto.response.ViewMyPageResponseDto;
import project.store.member.auth.JwtTokenProvider;
import project.store.member.auth.TokenInfo;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  @Value("${auth.mail.mark}")
  private String authMark;

  private final EmailUtil emailUtil;
  private final RedisUtil redisUtil;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final EncryptUtil encryptUtil;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  public static String createNumber(){
    return String.valueOf((int)(Math.random() * (90000) + 100000));// (int) Math.random() * (최댓값-최소값+1) + 최소값
  }
  @Async
  public String sendEmailCode(String email)  {
    memberRepository.findByEmail(email).ifPresent(member -> {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    });

    try {
      String number = createNumber();
      redisUtil.setDataExpire(email, number, 300);
      String title = "인증번호 메일입니다.";
      String body = "";
      body += "요청하신 인증 번호는 " + number + " 입니다.";
      emailUtil.sendEmail(email, title, body);
      return "메일 전송에 성공하였습니다.";
    } catch (Exception e) {
      return "메일 전송에 실패했습니다. 다시 시도해주세요.";
    }
  }

  public String verifyAuthCode(String email, String authCode) {
    String message = "";
    if(redisUtil.getData(email) != null && redisUtil.getData(email).equals(authCode)) {
      message += "이메일 인증이 완료되었습니다.";
      redisUtil.setDataExpire(email,authMark,1800);
    } else {
      message += "인증번호가 유효하지 않습니다.";
    }
    return message;
  }

  public String joinMember(JoinRequestDto joinRequestDto) {
    String emailAuthResult = redisUtil.getData(joinRequestDto.getEmail());
    if(emailAuthResult == null || !emailAuthResult.equals(authMark)) {
      return "이메일 인증 다시 시도 요구";
    } else {
      joinRequestDto.passwordEncoding(passwordEncoder);
      joinRequestDto.userInfoEncrypt(encryptUtil);
      memberRepository.save(joinRequestDto.toEntity());
      return "회원가입 완료";
    }
  }

  public TokenInfo loginMember(LoginRequestDto loginRequestDto) {
    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(
        loginRequestDto.getEmail(),   loginRequestDto.getPassword()
    );
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    return jwtTokenProvider.createToken(authentication);
  }

  public TokenInfo refreshToken(String refreshToken) {
    if(jwtTokenProvider.validateToken(refreshToken)) {
      TokenInfo token = jwtTokenProvider.refreshToken(refreshToken);
      return token;
    } else {
      return null;
    }
  }

  public String logout(String email, String refreshToken) {
    if(jwtTokenProvider.validateToken(refreshToken)) {
     redisUtil.deleteListData(encryptUtil.encrypt(email), refreshToken, 30);
      return "로그아웃 성공";
    } else {
      return "로그아웃 실패";
    }
  }

  public String logoutAll(String email) {
      redisUtil.deleteData(encryptUtil.encrypt(email));
      return "로그아웃 성공";
  }

  public ViewMyPageResponseDto viewMyPage(Long id) {
    Optional<Member> member = memberRepository.findById(id);
    return ViewMyPageResponseDto.toDto(member.orElseThrow(), encryptUtil);
  }

  public Optional<Member> updateMyPage(Long id, UpdateMyPageRequestDto updateMyPageRequestDto) {
    Optional<Member> member = memberRepository.findById(id);
    member.ifPresent(value -> {
      updateMyPageRequestDto.toUpdateEntity(value, encryptUtil);
      memberRepository.save(value);
    });
    return member;
  }

  public String changePassword(Long id, String password) {
    Optional<Member> member = memberRepository.findById(id);
    member.ifPresent(value -> {
      value.setPassword(passwordEncoder.encode(password));
      memberRepository.save(value);
    });
    return "비밀번호 변경 성공";
  }
}
