package project.store.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.api.dto.request.PaymentRequestDto;
import project.store.api.dto.response.CommonResponseDto;
import project.store.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping
  public CommonResponseDto<?> pendingPayment(
    @RequestHeader("id") Long memberId,
    @RequestBody PaymentRequestDto paymentRequestDto
    ) {
    paymentService.PendingPayment(memberId, paymentRequestDto);
    return new CommonResponseDto<>();
  }

  @PutMapping("/complete/{orderId}")
  public CommonResponseDto<?> completePayment(
    @PathVariable Long orderId
    ) {
    paymentService.completePayment(orderId);
    return new CommonResponseDto<>();
  }

}
