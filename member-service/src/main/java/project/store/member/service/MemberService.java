package project.store.member.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.api.dto.request.LoginRequestDto;
import project.store.member.api.dto.request.UpdateMyPageRequestDto;
import project.store.member.api.dto.response.ClothDetailResponseDto;
import project.store.member.api.dto.response.OrderListResponseDto;
import project.store.member.api.dto.response.ViewMyPageResponseDto;
import project.store.member.api.dto.response.WishListResponseDto;
import project.store.member.auth.JwtTokenProvider;
import project.store.member.auth.TokenInfo;
import project.store.member.client.OrderServiceClient;
import project.store.member.common.CommonResponseDto;
import project.store.member.common.exception.CustomException;
import project.store.member.common.exception.MemberExceptionEnum;
import project.store.member.common.util.EmailUtil;
import project.store.member.common.util.EncryptUtil;
import project.store.member.common.util.RedisUtil;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  @Value("${auth.mail.mark}")
  private String authMark;

  private final OrderServiceClient orderServiceClient;
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
  public void sendEmailCode(String email)  {
    memberRepository.findByEmail(email).ifPresent(member -> {
      throw new CustomException(MemberExceptionEnum.MEMBER_ALREADY_EXISTS);
    });

    try {
      String number = createNumber();
      redisUtil.setDataExpire(email, number, 300);
      String title = "인증번호 메일입니다.";
      String body = "";
      body += "요청하신 인증 번호는 " + number + " 입니다.";
      emailUtil.sendEmail(email, title, body);
    } catch (Exception e) {
      throw new CustomException(MemberExceptionEnum.EMAIL_SEND_FAIL);
    }
  }

  public void verifyAuthCode(String email, String authCode) {
    if(redisUtil.getData(email) != null && redisUtil.getData(email).equals(authCode)) {
      redisUtil.setDataExpire(email,authMark,1800);
    } else {
      throw new CustomException(MemberExceptionEnum.EMAIL_CODE_NOT_MATCH);
    }
  }

  public Member joinMember(JoinRequestDto joinRequestDto) {
    String emailAuthResult = redisUtil.getData(joinRequestDto.getEmail());
    if(emailAuthResult == null || !emailAuthResult.equals(authMark)) {
      throw new CustomException(MemberExceptionEnum.EMAIL_CODE_NOT_MATCH);
    } else {
      joinRequestDto.passwordEncoding(passwordEncoder);
      joinRequestDto.userInfoEncrypt(encryptUtil);
      return memberRepository.save(joinRequestDto.toEntity());
    }
  }

  public TokenInfo loginMember(LoginRequestDto loginRequestDto) {
    try {
      UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
          loginRequestDto.getEmail(), loginRequestDto.getPassword()
        );
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      return jwtTokenProvider.createToken(authentication);
    } catch (Exception e) {
      throw new CustomException(MemberExceptionEnum.LOGIN_FAILED);
    }
  }

  public TokenInfo refreshToken(Long memberId, String refreshToken) {
    if(jwtTokenProvider.validateToken(refreshToken) && redisUtil.existListData(String.valueOf(memberId), refreshToken)) {
      TokenInfo token = jwtTokenProvider.refreshToken(refreshToken);
      return token;
    } else {
      throw new CustomException(MemberExceptionEnum.REFRESH_TOKEN_EXPIRED);
    }
  }

  public void logout(Long memberId, String refreshToken) {
     redisUtil.deleteListData(String.valueOf(memberId), refreshToken, 30);
  }

  public void logoutAll(Long memberId) {
      redisUtil.deleteData(String.valueOf(memberId));
  }

  public ViewMyPageResponseDto viewMyPage(Long id) {
    Optional<Member> member = memberRepository.findById(id);
    return ViewMyPageResponseDto.toDto(member.orElseThrow(
      () -> new CustomException(MemberExceptionEnum.MEMBER_NOT_FOUND)
    ), encryptUtil);
  }

  public void updateMyPage(Long id, UpdateMyPageRequestDto updateMyPageRequestDto) {
    Member member = memberRepository.findById(id).orElseThrow(
      () -> new CustomException(MemberExceptionEnum.MEMBER_NOT_FOUND));
      updateMyPageRequestDto.toUpdateEntity(member, encryptUtil);
      memberRepository.save(member);
  }

  public void changePassword(Long id, String password) {
    Member member = memberRepository.findById(id).orElseThrow(
      () -> new CustomException(MemberExceptionEnum.MEMBER_NOT_FOUND));
      member.setPassword(passwordEncoder.encode(password));
      memberRepository.save(member);
  }

  public List<OrderListResponseDto> getMyOrderList(Long id) {
    CommonResponseDto<List<OrderListResponseDto>> list = orderServiceClient.getOrderList(id);
    return list.getData();
  }

  public List<WishListResponseDto> getMyWishList(Long id) {
    CommonResponseDto<List<WishListResponseDto>> list = orderServiceClient.getWishList(id);
    return list.getData();
  }

}
