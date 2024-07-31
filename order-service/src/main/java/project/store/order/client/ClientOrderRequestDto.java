package project.store.order.client;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderRequestDto {
  private Long orderId;
  private Long clothDetailId;
  private int quantity;

}
