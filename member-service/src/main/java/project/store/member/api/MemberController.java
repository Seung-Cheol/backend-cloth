package project.store.member.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.List;
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
import project.store.member.api.dto.response.ClothDetailResponseDto;
import project.store.member.api.dto.response.OrderListResponseDto;
import project.store.member.api.dto.response.WishListResponseDto;
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
    memberService.sendEmailCode(email);
    return new CommonResponseDto<>();
  }

  @PostMapping("/email/verify")
  public CommonResponseDto<?> verifyCode(@Email String email, String authCode) {
    memberService.verifyAuthCode(email,authCode);
    return new CommonResponseDto<>();
  }

  @PostMapping("/join")
  public CommonResponseDto<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
    memberService.joinMember(joinRequestDto);
    return new CommonResponseDto<>();
  }

  @PostMapping("/login")
  public CommonResponseDto<TokenInfo> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    TokenInfo tokenInfo = memberService.loginMember(loginRequestDto);
    return CommonResponseDto.ofSuccess(tokenInfo);
  }

  @GetMapping("/refresh")
  public CommonResponseDto<TokenInfo> refresh(Long memberId, String refreshToken) {
    TokenInfo tokenInfo = memberService.refreshToken(memberId, refreshToken);
    return CommonResponseDto.ofSuccess(tokenInfo);
  }

  @GetMapping("/mypage")
  public CommonResponseDto<ViewMyPageResponseDto> myPage(@RequestHeader("id") Long memberId) {
    ViewMyPageResponseDto data = memberService.viewMyPage(memberId);
    return CommonResponseDto.ofSuccess(data);
  }

  @PutMapping("/mypage")
  public CommonResponseDto<?> updateMyPage(
    @RequestHeader("id") Long memberId,
    @RequestBody UpdateMyPageRequestDto updateMyPageRequestDto) {
    memberService.updateMyPage(memberId, updateMyPageRequestDto);
    return new CommonResponseDto<>();
  }

  @PutMapping("/password")
  public CommonResponseDto<?> changePassword(
    @RequestHeader("id") Long memberId, String password) {
    memberService.changePassword(memberId, password);
    memberService.logoutAll(memberId);
    return new CommonResponseDto<>();
  }

  @DeleteMapping("/logout")
  public CommonResponseDto<?> logout(@RequestHeader("id") Long memberId, String refreshToken) {
    memberService.logout(memberId,refreshToken);
    return new CommonResponseDto<>();
  }

  @DeleteMapping("/logout/all")
  public CommonResponseDto<?> logoutAll(@RequestHeader("id") Long memberId) {
    memberService.logoutAll(memberId);
    return new CommonResponseDto<>();
  }


  @GetMapping("/orderlist")
  public CommonResponseDto<List<OrderListResponseDto>> getOrderList(@RequestHeader("id") Long memberId) {
    List<OrderListResponseDto> data = memberService.getMyOrderList(memberId);
    return CommonResponseDto.ofSuccess(data);
  }

  @GetMapping("/wishlist")
  public CommonResponseDto<List<WishListResponseDto>> getWishList(@RequestHeader("id") Long memberId) {
    List<WishListResponseDto> data = memberService.getMyWishList(memberId);
    return CommonResponseDto.ofSuccess(data);
  }


}
