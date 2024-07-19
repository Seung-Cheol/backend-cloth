package project.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

  public Payment updateStatus(PaymentStatus status) {
    this.status = status;
    return this;
  }
}
