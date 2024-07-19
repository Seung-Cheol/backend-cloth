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

@Component
@RequiredArgsConstructor
public class OrderConsumer {
  private final OrderRepository orderRepository;
  private final OrderOutboxRepository orderOutboxRepository;
  private final OrderProducer orderProducer;
  private final String topic = "order_complete";
  private final String rollbackTopic = "payment_rollback";

  @KafkaListener(topics = "payment_complete")
  @Transactional
  public void consume(String message) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Long orderId = Long.parseLong(message);
    try {
      Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

      List<ClothDetailDto> dtos = order.getOrderCloths().stream()
        .map(orderCloth -> ClothDetailDto.builder()
          .orderId(order.getId())
          .clothDetailId(orderCloth.getClothDetailId())
          .quantity(orderCloth.getOrderClothCount())
          .build())
        .collect(Collectors.toList());

      order.updateStatus(OrderStatus.PAID);
      orderRepository.save(order);

      saveOrderOutbox(topic, objectMapper.writeValueAsString(dtos));
    } catch (Exception e) {
      saveOrderOutbox(rollbackTopic, message);
      throw e;
    }
    orderProducer.send();
  }

  private void saveOrderOutbox(String topic, String message) {
    OrderOutbox outboxEntry = OrderOutbox.builder()
      .topic(topic)
      .message(message)
      .isSent(false)
      .build();
    orderOutboxRepository.save(outboxEntry);
  }
}
