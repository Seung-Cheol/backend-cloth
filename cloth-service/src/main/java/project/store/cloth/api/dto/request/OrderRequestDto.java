package project.store.cloth.api.dto.request;


import lombok.Data;
import lombok.Getter;

@Data
public class OrderRequestDto {
  private Long orderId;
  private Long clothDetailId;
  private int quantity;

}
