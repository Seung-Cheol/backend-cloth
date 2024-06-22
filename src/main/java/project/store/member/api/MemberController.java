package project.store.member.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.member.api.dto.request.JoinRequestDto;
import project.store.member.api.dto.response.CommonResponseDto;
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


}
