package project.store.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.store.api.dto.request.PaymentRequestDto;
import project.store.common.exception.CustomException;
import project.store.common.exception.PaymentExceptionEnum;
import project.store.domain.Payment;
import project.store.domain.PaymentOutbox;
import project.store.domain.PaymentStatus;
import project.store.domain.repository.PaymentOutboxRepository;
import project.store.domain.repository.PaymentRepository;
import project.store.kafka.PaymentProducer;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentOutboxRepository paymentOutboxRepository;
  private final PaymentProducer paymentProducer;
  private final String topic = "payment_complete";
  public Payment PendingPayment(Long memberId, PaymentRequestDto paymentRequestDto) {
    return paymentRepository.save(paymentRequestDto.toEntity(memberId));
  }


  @Transactional
  public void completePayment(Long orderId) {
    Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new CustomException(PaymentExceptionEnum.ORDER_NOT_FOUND));

    payment.updateStatus(PaymentStatus.SUCCESS);
    paymentRepository.save(payment);
    paymentOutboxRepository.save(PaymentOutbox.builder()
      .orderId(orderId)
      .topic(topic)
      .isSent(false)
      .build());
    paymentProducer.send();

  }

}
