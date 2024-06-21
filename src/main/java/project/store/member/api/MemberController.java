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
import project.store.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/email/send")
  public ResponseEntity<?> sendEmail(@Email String email) {
    memberService.sendEmailCode(email);
    return ResponseEntity.status(201).build();
  }

  @PostMapping("/email/verify")
  public ResponseEntity<?> verifyCode(String email, String authCode) {
    String message = memberService.verifyAuthCode(email,authCode);
    return ResponseEntity.status(201).body(message);
  }

  @PostMapping("/join")
  public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {

    return ResponseEntity.status(201).build();
  }


}
