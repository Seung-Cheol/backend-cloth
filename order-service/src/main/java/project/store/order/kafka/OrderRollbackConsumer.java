package project.store.order.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.repository.OrderRepository;
import project.store.order.service.OrderService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderRollbackConsumer {

  private final OrderRepository orderRepository;


  @KafkaListener(topics = "order_rollback")
  public void consumeOrderRollback(String message) {
    Long orderId = Long.parseLong(message);
    orderRepository.findById(orderId)
      .switchIfEmpty(Mono.error(new IllegalArgumentException("주문 정보가 없습니다.")))
      .doOnNext(order -> order.updateStatus(OrderStatus.CANCEL))
      .flatMap(orderRepository::save)
      .subscribe();
  }

}
