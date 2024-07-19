package project.store.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import project.store.common.exception.CustomException;
import project.store.common.exception.PaymentExceptionEnum;
import project.store.domain.Payment;
import project.store.domain.PaymentStatus;
import project.store.domain.repository.PaymentRepository;

@Component
@RequiredArgsConstructor
public class PaymentRollbackConsumer {

  private final PaymentRepository paymentRepository;

  @KafkaListener(topics = "payment_rollback")
  public void consumePaymentRollback(String message) throws JsonProcessingException {
    Long orderId = Long.parseLong(message);
    Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new CustomException(
      PaymentExceptionEnum.ORDER_NOT_FOUND));
    payment.updateStatus(PaymentStatus.FAILED);
    paymentRepository.save(payment);
  }

}
