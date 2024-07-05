package project.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Builder
@Getter
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long orderId;
  private Long memberId;
  private int price;
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
  @UpdateTimestamp
  private LocalDateTime completedAt;

  public Payment(Long id, Long orderId, Long memberId, int price, PaymentStatus status) {
    this.id = id;
    this.orderId = orderId;
    this.memberId = memberId;
    this.price = price;
    this.status = status;
  }

  public Payment updateStatus(PaymentStatus status) {
    this.status = status;
    return this;
  }
}
