package project.store.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.store.api.dto.request.PaymentRequestDto;
import project.store.domain.Payment;
import project.store.domain.PaymentOutbox;
import project.store.domain.PaymentStatus;
import project.store.domain.repository.PaymentOutboxRepository;
import project.store.domain.repository.PaymentRepository;
import project.store.kafka.PaymentProducer;
import project.store.service.PaymentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PaymentServiceTest {

  private PaymentService paymentService;
  private PaymentRepository paymentRepository;
  private PaymentOutboxRepository paymentOutboxRepository;
  private PaymentProducer paymentProducer;

  private Payment 결제;
  private PaymentOutbox 아웃박스;

  @BeforeEach
  void setUp() {
    //mocking
    paymentRepository = Mockito.mock(PaymentRepository.class);
    paymentOutboxRepository = Mockito.mock(PaymentOutboxRepository.class);
    paymentProducer = Mockito.mock(PaymentProducer.class);
    paymentService = new PaymentService(
      paymentRepository,paymentOutboxRepository,paymentProducer
    );
    결제 = Payment.builder()
      .orderId(1L)
      .id(1L)
      .status(PaymentStatus.PENDING)
      .price(10000)
      .memberId(1L)
      .build();

    아웃박스 = PaymentOutbox.builder()
      .orderId(1L)
      .id(1L)
      .topic("payment_complete")
      .isSent(false)
      .build();


  }

  @DisplayName("결제 진입")
  @Test
  void paymentEntry() {
    // given
    when(paymentRepository.save(any(Payment.class))).thenReturn(결제);
    // when
    Payment payment = paymentService.PendingPayment(1L, PaymentRequestDto.builder().price(10000).orderId(1L).build());
    // then
    assertEquals(payment.getStatus(), PaymentStatus.PENDING);

  }


  @DisplayName("결제 성공")
  @Test
  void paymentSuccess() {
    // given
    when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(결제));
    when(paymentOutboxRepository.save(any(PaymentOutbox.class))).thenReturn(아웃박스);
    // when
    paymentService.completePayment(1L);
    // then
    assertEquals(결제.getStatus(), PaymentStatus.SUCCESS);
  }

}
