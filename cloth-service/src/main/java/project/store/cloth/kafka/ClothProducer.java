package project.store.cloth.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.store.cloth.domain.ClothOutbox;
import project.store.cloth.domain.repository.ClothOutboxRepository;

@Component
@RequiredArgsConstructor
public class ClothProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ClothOutboxRepository clothOutboxRepository;

  @Scheduled(fixedRate = 1000 * 60 * 3)
  public void send() {
    List<ClothOutbox> messagesToPublish = clothOutboxRepository.findByIsSentFalse();
    ObjectMapper objectMapper = new ObjectMapper();
    for (ClothOutbox message : messagesToPublish) {
      try {
        kafkaTemplate.send(message.getTopic(), objectMapper.writeValueAsString(message.getMessage()));
        message.isSent();
        clothOutboxRepository.save(message);
      } catch (Exception e) {

      }
    }
  }
}
