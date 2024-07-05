package project.store.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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

  public void send() {
        // isSent가 false인 모든 outbox 메시지를 조회
        List<PaymentOutbox> messagesToPublish = paymentOutboxRepository.findByIsSentFalse();

      for (PaymentOutbox message : messagesToPublish) {
        try {
          // 메시지를 카프카에 발행
          kafkaTemplate.send(message.getTopic(), String.valueOf(message.getOrderId()));
          // 발행 성공 후, isSent를 true로 업데이트
          message.updateSent(true);
          paymentOutboxRepository.save(message);
        } catch (Exception e) {

        }
      }
  }
}
