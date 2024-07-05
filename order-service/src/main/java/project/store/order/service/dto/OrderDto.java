package project.store.order.service.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderAddress;
import project.store.order.domain.entity.OrderStatus;


@Getter
@Builder
public class OrderDto {

  @Enumerated(EnumType.STRING)
  private OrderAddress orderAddress;

  private Long memberId;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  public Order createOrder(OrderDto orderDto) {
    return Order.builder()
      .orderAddress(orderDto.getOrderAddress())
      .memberId(orderDto.getMemberId())
      .orderStatus(orderDto.getOrderStatus())
      .build();
  }
}
