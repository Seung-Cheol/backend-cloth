package project.store.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.store.domain.Payment;
import project.store.domain.PaymentStatus;

@Getter
@Setter
@Builder
public class PaymentRequestDto {
  @NotBlank
  @Min(0)
  private int price;
  @NotBlank
  private Long orderId;

  public Payment toEntity(Long memberId) {
    return Payment.builder()
      .orderId(orderId)
      .memberId(memberId)
      .price(price)
      .status(PaymentStatus.PENDING)
      .build();
  }

}
