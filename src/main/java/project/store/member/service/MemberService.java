package project.store.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.store.common.util.EmailUtil;
import project.store.common.util.EncryptUtil;
import project.store.common.util.RedisStringUtil;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  @Value("${auth.mail.mark}")
  private String authMark;

  private final EmailUtil emailUtil;
  private final RedisStringUtil redisStringUtil;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final EncryptUtil encryptUtil;

  public static String createNumber(){
    return String.valueOf((int)(Math.random() * (90000) + 100000));// (int) Math.random() * (최댓값-최소값+1) + 최소값
  }
  @Async
  public String sendEmailCode(String email)  {
    if(memberRepository.findByEmail(email) != null) {
      return "이미 존재하는 메일입니다";
    }

    try {
      String number = createNumber();
      redisStringUtil.setDataExpire(email, number, 300);
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
    if(redisStringUtil.getData(email) != null && redisStringUtil.getData(email).equals(authCode)) {
      message += "이메일 인증이 완료되었습니다.";
      redisStringUtil.setDataExpire(email,authMark,1800);
    } else {
      message += "인증번호가 유효하지 않습니다.";
    }
    return message;
  }

  public String joinMember(JoinRequestDto joinRequestDto) {
    String emailAuthResult = redisStringUtil.getData(joinRequestDto.getEmail());
    if(emailAuthResult == null || !emailAuthResult.equals(authMark)) {
      return "이메일 인증 다시 시도 요구";
    } else {
      joinRequestDto.passwordEncoding(passwordEncoder);
      joinRequestDto.userInfoEncrypt(encryptUtil);
      memberRepository.save(joinRequestDto.toEntity());
      return "회원가입 완료";
    }
  }

}
