package project.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class PaymentOutbox {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_outbox_id")
  public Long id;
  public Long orderId;
  public String topic;
  public boolean isSent;

  public PaymentOutbox(String topic, Long orderId, boolean isSent) {
    this.topic = topic;
    this.isSent = isSent;
    this.orderId = orderId;
  }

  public void updateSent(boolean isSent) {
    this.isSent = isSent;
  }
}
