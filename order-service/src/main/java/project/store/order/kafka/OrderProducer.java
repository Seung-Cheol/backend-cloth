package project.store.order.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.store.order.domain.entity.OrderOutbox;
import project.store.order.domain.repository.OrderOutboxRepository;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderOutboxRepository orderOutboxRepository;

    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void send() {
        List<OrderOutbox> messagesToPublish = orderOutboxRepository.findByIsSentFalse();
        ObjectMapper objectMapper = new ObjectMapper();
        for (OrderOutbox message : messagesToPublish) {
            try {
                kafkaTemplate.send(message.getTopic(), objectMapper.writeValueAsString(message.getMessage()));
                message.isSent();
                orderOutboxRepository.save(message);
            } catch (Exception e) {

            }
        }
    }
}
