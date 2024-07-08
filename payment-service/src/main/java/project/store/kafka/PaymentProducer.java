package project.store.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.store.domain.PaymentOutbox;
import project.store.domain.repository.PaymentOutboxRepository;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final PaymentOutboxRepository paymentOutboxRepository;
  private final String topic = "payment_complete";


  @Scheduled(fixedRate = 1000 * 60 * 3)
  public void send() {
    List<PaymentOutbox> messagesToPublish = paymentOutboxRepository.findByIsSentFalse();
    for (PaymentOutbox message : messagesToPublish) {
      try {
        kafkaTemplate.send(message.getTopic(), String.valueOf(message.getOrderId()));
        message.isSent();
        paymentOutboxRepository.save(message);
      } catch (Exception e) {

      }
    }
  }
}
