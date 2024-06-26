package project.store.order.api.dto.request;


import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class WishListUpdateRequestDto {
  private Long WishListId;

  @Min(1)
  private int quantity;
}
