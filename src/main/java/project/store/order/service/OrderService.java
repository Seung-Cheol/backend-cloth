package project.store.order.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.store.member.domain.Member;
import project.store.member.domain.repository.MemberRepository;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.repository.OrderClothRepository;
import project.store.order.domain.repository.OrderRepository;
import project.store.order.service.dto.OrderClothDto;
import project.store.order.service.dto.OrderDto;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final OrderClothRepository orderClothRepository;

  public Order createOrder(OrderDto orderDto) {
    return orderRepository.save(orderDto.createOrder(orderDto));
  }

  public void createOrderCloth(List<OrderClothDto> orderClothDtos) {
    for(OrderClothDto orderClothDto : orderClothDtos) {
      orderClothRepository.save(orderClothDto.createOrderCloth(orderClothDto));
    }
  }

  public Order orderByMemberId(Long memberId) {
    Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
    return orderRepository.findByMember(member).orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));
  }

  public void updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));
    order.updateStatus(status);
    orderRepository.save(order);
  }

  public List<OrderCloth> getOrderClothByOrderId(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));
    return orderClothRepository.findByOrder(order);
  }

  public Order getOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));
  }

}
