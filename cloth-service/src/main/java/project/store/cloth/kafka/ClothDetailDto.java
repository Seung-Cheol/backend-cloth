package project.store.cloth.kafka;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClothDetailDto {
  private long orderId;
  private long clothDetailId;
  private int quantity;

  public ClothDetailDto(long orderId, long clothDetailId, int quantity) {
    this.orderId = orderId;
    this.clothDetailId = clothDetailId;
    this.quantity = quantity;
  }
}
