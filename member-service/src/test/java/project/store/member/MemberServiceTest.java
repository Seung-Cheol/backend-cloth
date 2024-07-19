package project.store.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.api.dto.request.LoginRequestDto;
import project.store.member.api.dto.response.ViewMyPageResponseDto;
import project.store.member.auth.TokenInfo;
import project.store.member.common.exception.CustomException;
import project.store.member.common.exception.MemberExceptionEnum;
import project.store.member.common.util.RedisUtil;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;
import project.store.member.service.MemberService;

  @SpringBootTest
  @Transactional
  @ActiveProfiles("test")
public class MemberServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private MemberService memberService;

  @Autowired
  private RedisUtil redisUtil;

  @Value("${auth.mail.mark}")
  private String authMark;

  @BeforeEach
  void setUp() {
    memberRepository.deleteAllInBatch();
    redisUtil.deleteData("rlatmdcjf");
  }


  @DisplayName("중복 회원에게 가입 인증 이메일 발송")
  @Test
  void isDuplicatedEmail_Send() {
    String email = "rlatmdcjf";

    // given
    Member member = Member.builder()
      .email(email)
      .build();
    memberRepository.save(member);
    // when
    CustomException customException = assertThrows(CustomException.class, () ->
      memberService.sendEmailCode(email)
    );

    // then
    assertEquals(customException.getPaymentExceptionEnum(), MemberExceptionEnum.MEMBER_ALREADY_EXISTS);

  }

  @DisplayName("인증번호 불일치")
  @Test
  void isNotMatchedAuthCode() {
    String email = "rlatmdcjf";
    String authCode = "123456";

    //given
    redisUtil.setDataExpire(email, "654321", 300);
    //when
    CustomException customException = assertThrows(CustomException.class, () ->
      memberService.verifyAuthCode(email, authCode)
    );
    //then
    assertEquals(customException.getPaymentExceptionEnum(), MemberExceptionEnum.EMAIL_CODE_NOT_MATCH);
  }


  @DisplayName("인증번호 일치")
  @Test
  void isMatchedAuthCode() {
    String email = "rlatmdcjf";
    String authCode = "123456";

    //given
    redisUtil.setDataExpire(email, authCode, 300);

    //when
    memberService.verifyAuthCode(email, authCode);

    //then
    assertEquals(redisUtil.getData(email), authMark);

  }

  @DisplayName("회원가입 시 인증 받지 않은 회원")
  @Test
  void isSignedUp_NotVerified() {

    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    //when
    CustomException customException = assertThrows(CustomException.class,
      () -> memberService.joinMember(joinRequestDto));

    //then
    assertEquals(customException.getPaymentExceptionEnum(),
      MemberExceptionEnum.EMAIL_CODE_NOT_MATCH);
  }

  @DisplayName("정상 회원가입")
  @Test
  void isSignedUp() {
      //given
      JoinRequestDto joinRequestDto = JoinRequestDto.builder()
        .email("rlatmdcjf")
        .password("123456")
        .nickname("godgod")
        .phone("010-1234-5678")
        .memberAddress(null)
        .build();
      redisUtil.setDataExpire("rlatmdcjf",authMark,300);

    //when
    Member member = memberService.joinMember(joinRequestDto);

    //then
    assertNotNull(member);

  }

  @DisplayName("로그인 성공")
  @Test
  void isLogin() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    memberService.joinMember(joinRequestDto);

    //when
    TokenInfo tokenInfo = memberService.loginMember(LoginRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .build());

    //then
    assertNotNull(tokenInfo.getAccessToken());
    assertNotNull(tokenInfo.getRefreshToken());
  }

  @DisplayName("로그인 실패")
  @Test
  void isFailLogin() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    memberService.joinMember(joinRequestDto);

    //when
    CustomException customException = assertThrows(CustomException.class, () ->
      memberService.loginMember(LoginRequestDto.builder()
        .email("rlatmdcjf")
        .password("1234567")
        .build())
    );

    //then
    assertEquals(customException.getPaymentExceptionEnum(), MemberExceptionEnum.LOGIN_FAILED);
  }

  @DisplayName("토큰 재발급")
  @Test
  void isRefreshToken() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    Member member = memberService.joinMember(joinRequestDto);
    TokenInfo tokenInfo = memberService.loginMember(LoginRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .build());

    //when
    TokenInfo token = memberService.refreshToken(member.getId(), tokenInfo.getRefreshToken());

    //then
    assertNotNull(token.getAccessToken());
    assertNotNull(token.getRefreshToken());
  }

  @DisplayName("로그아웃")
  @Test
  void isLogout() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    Member member = memberService.joinMember(joinRequestDto);
    TokenInfo tokenInfo = memberService.loginMember(LoginRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .build());
    long id = member.getId();

    //when
    memberService.logout(id, tokenInfo.getRefreshToken());
    CustomException customException = assertThrows(CustomException.class, () -> memberService.refreshToken(
      member.getId(), tokenInfo.getRefreshToken()));
    //then
    assertEquals(customException.getPaymentExceptionEnum(), MemberExceptionEnum.REFRESH_TOKEN_EXPIRED);

  }

  @DisplayName("전체 로그아웃")
  @Test
  void isLogoutAll() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    Member member = memberService.joinMember(joinRequestDto);
    TokenInfo tokenInfo = memberService.loginMember(LoginRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .build());
    long id = member.getId();

    //when
    memberService.logoutAll(id);
    //then
    assertNull(redisUtil.getData(String.valueOf(id)));
  }

  @DisplayName("마이페이지 조회")
  @Test
  void isViewMyPage() {
    //given
    JoinRequestDto joinRequestDto = JoinRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .nickname("godgod")
      .phone("010-1234-5678")
      .memberAddress(null)
      .build();
    redisUtil.setDataExpire("rlatmdcjf",authMark,300);
    Member member = memberService.joinMember(joinRequestDto);
    TokenInfo tokenInfo = memberService.loginMember(LoginRequestDto.builder()
      .email("rlatmdcjf")
      .password("123456")
      .build());
    long id = member.getId();

    //when
    ViewMyPageResponseDto myPage = memberService.viewMyPage(id);

    //then
    assertEquals(myPage.getEmail(), "rlatmdcjf");
    assertEquals(myPage.getPhone(), "010-1234-5678");
  }



}
