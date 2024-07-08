package project.store.order.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import project.store.order.domain.entity.OrderAddress;

@Getter
@Setter
public class OrderRequestDto {
  @Min(1)
  @NotBlank
  private int quantity;

  @NotBlank
  private Long clothDetailId;

  @NotBlank
  private OrderAddress orderAddress;

}
