package project.store.order.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderOutbox;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.repository.OrderOutboxRepository;
import project.store.order.domain.repository.OrderRepository;
import project.store.order.kafka.dto.ClothDetailDto;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
  private final OrderRepository orderRepository;
  private final OrderOutboxRepository orderOutboxRepository;
  private final OrderProducer orderProducer;
  private final String topic = "order_complete";
  private final String rollbackTopic = "payment_rollback";

  @KafkaListener(topics = "payment_complete")
  public void consume(String message) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Long orderId = Long.parseLong(message);
    orderRepository.findById(orderId)
      .switchIfEmpty(Mono.error(new EntityNotFoundException("Order not found for ID: " + orderId)))
      .flatMap(order -> {
        List<ClothDetailDto> dtos = order.getOrderCloths().stream()
          .map(orderCloth -> ClothDetailDto.builder()
            .orderId(order.getId())
            .clothDetailId(orderCloth.getClothDetailId())
            .quantity(orderCloth.getOrderClothCount())
            .build())
          .collect(Collectors.toList());

        order.updateStatus(OrderStatus.PAID);
        try {
          return orderRepository.save(order)
            .then(saveOrderOutbox(topic, objectMapper.writeValueAsString(dtos)));
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      })
      .onErrorResume(e -> saveOrderOutbox(rollbackTopic, message).then(Mono.error(e)))
      .doOnTerminate(orderProducer::send)
      .subscribe();
  }

  private Mono<OrderOutbox> saveOrderOutbox(String topic, String message) {
    OrderOutbox outboxEntry = OrderOutbox.builder()
      .topic(topic)
      .message(message)
      .isSent(false)
      .build();
    return orderOutboxRepository.save(outboxEntry);
  }
}
