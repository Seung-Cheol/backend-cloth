package project.store.order.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
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

  @KafkaListener(topics = "payment_complete")
  @Transactional
  public void consume(String message) throws JsonProcessingException {
    Long orderId = Long.parseLong(message);
    List<ClothDetailDto> dtos = new ArrayList<>();
    orderRepository.findById(orderId).ifPresent(order -> {
      order.getOrderCloths().forEach(orderCloth -> {
        dtos.add(ClothDetailDto.builder()
          .orderId(order.getId())
          .clothDetailId(orderCloth.getClothDetailId())
          .quantity(orderCloth.getOrderClothCount())
          .build());
      });
      order.updateStatus(OrderStatus.PAID);
      orderRepository.save(order);
    });

    ObjectMapper objectMapper = new ObjectMapper();
      orderOutboxRepository.save(OrderOutbox.builder()
        .topic(topic)
        .message(objectMapper.writeValueAsString(dtos))
        .isSent(false)
        .build());

    orderProducer.send();
    }
}
