package project.store.member.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.api.dto.request.LoginRequestDto;
import project.store.member.api.dto.request.UpdateMyPageRequestDto;
import project.store.member.common.CommonResponseDto;
import project.store.member.api.dto.response.ViewMyPageResponseDto;
import project.store.member.common.CustomMember;
import project.store.member.auth.TokenInfo;
import project.store.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/email/send")
  public CommonResponseDto<?> sendEmail(@Email String email) {
    String message = memberService.sendEmailCode(email);
    return CommonResponseDto.of(message);
  }

  @PostMapping("/email/verify")
  public CommonResponseDto<?> verifyCode(String email, String authCode) {
    String message = memberService.verifyAuthCode(email,authCode);
    return CommonResponseDto.of(message);
  }

  @PostMapping("/join")
  public CommonResponseDto<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
    String message = memberService.joinMember(joinRequestDto);
    return CommonResponseDto.of(message);
  }

  @PostMapping("/login")
  public CommonResponseDto<TokenInfo> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    TokenInfo tokenInfo = memberService.loginMember(loginRequestDto);
    return CommonResponseDto.ofData("로그인 성공", tokenInfo);
  }

  @GetMapping("/refresh")
  public CommonResponseDto<TokenInfo> refresh(String refreshToken) {
    TokenInfo tokenInfo = memberService.refreshToken(refreshToken);
    return CommonResponseDto.ofData("토큰 갱신 성공", tokenInfo);
  }

  @GetMapping("/mypage")
  public CommonResponseDto<ViewMyPageResponseDto> myPage(@RequestHeader("id") Long memberId) {
    ViewMyPageResponseDto data = memberService.viewMyPage(memberId);
    return CommonResponseDto.ofData("마이페이지 조회 성공", data);
  }

  @PutMapping("/mypage")
  public CommonResponseDto<?> updateMyPage(
    @AuthenticationPrincipal CustomMember customMember,
    @RequestBody UpdateMyPageRequestDto updateMyPageRequestDto) {
    memberService.updateMyPage(customMember.getId(), updateMyPageRequestDto);
    return CommonResponseDto.ofData("마이페이지 수정 성공", updateMyPageRequestDto);
  }

  @PutMapping("/password")
  public CommonResponseDto<?> changePassword(
    @AuthenticationPrincipal CustomMember customMember, String password) {
    String message = memberService.changePassword(customMember.getId(), password);
    memberService.logoutAll(customMember.getEmail());
    return CommonResponseDto.of(message);
  }

  @DeleteMapping("/logout")
  public CommonResponseDto<?> logout(@AuthenticationPrincipal CustomMember customMember, String refreshToken) {
    String message = memberService.logout(customMember.getEmail(),refreshToken);
    return CommonResponseDto.of(message);
  }

  @DeleteMapping("/logout/all")
  public CommonResponseDto<?> logoutAll(@AuthenticationPrincipal CustomMember customMember) {
    String message = memberService.logoutAll(customMember.getEmail());
    return CommonResponseDto.of(message);
  }


}
