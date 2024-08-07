package project.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
public class PaymentOutbox {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_outbox_id")
  public Long id;
  public Long orderId;
  public String topic;
  public boolean isSent;

  public PaymentOutbox(Long id, Long orderId, String topic, boolean isSent) {
    this.topic = topic;
    this.isSent = isSent;
    this.orderId = orderId;
  }

  public void isSent() {
    this.isSent = true;
  }
}
