package project.store.order.service.dto;


import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import project.store.cloth.domain.ClothDetail;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;

@Getter
@Builder
public class OrderClothDto {

  @Min(1)
  private int quantity;

  private int price;

  private Order order;

  private ClothDetail clothDetail;

  public OrderCloth createOrderCloth(OrderClothDto orderClothDto) {
    return OrderCloth.builder()
      .orderClothCount(orderClothDto.getQuantity())
      .orderClothPrice(orderClothDto.getPrice())
      .order(orderClothDto.getOrder())
      .clothDetail(orderClothDto.getClothDetail())
      .build();
  }
}
