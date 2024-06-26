package project.store.order.service.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import project.store.member.domain.Member;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderAddress;
import project.store.order.domain.entity.OrderStatus;


@Getter
@Builder
public class OrderDto {

  @Enumerated(EnumType.STRING)
  private OrderAddress orderAddress;

  private Member member;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  public Order createOrder(OrderDto orderDto) {
    return Order.builder()
      .orderAddress(orderDto.getOrderAddress())
      .member(orderDto.getMember())
      .orderStatus(orderDto.getOrderStatus())
      .build();
  }
}
