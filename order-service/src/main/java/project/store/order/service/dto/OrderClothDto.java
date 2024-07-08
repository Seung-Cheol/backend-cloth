package project.store.order.service.dto;


import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;

@Getter
@Builder
public class OrderClothDto {

  @Min(1)
  private int quantity;

  private Order order;

  private Long clothDetailId;

  public OrderCloth createOrderCloth(OrderClothDto orderClothDto) {
    return OrderCloth.builder()
      .orderClothCount(orderClothDto.getQuantity())
      .order(orderClothDto.getOrder())
      .clothDetailId(orderClothDto.getClothDetailId())
      .build();
  }
}
