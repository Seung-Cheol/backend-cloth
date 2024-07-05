package project.store.order.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import project.store.order.domain.entity.OrderAddress;

@Getter
public class OrderFromWishListRequestDto {
  @NotBlank
  private Long[] wishListIds;
  @NotBlank
  private OrderAddress orderAddress;

}
