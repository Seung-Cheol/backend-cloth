package project.store.order.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import project.store.order.kafka.dto.ClothDetailDto;

@Entity
@Builder
@Getter
@NoArgsConstructor
public class OrderOutbox {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String topic;
  private String message;
  private boolean isSent;

  public OrderOutbox(Long id,String topic, String message, boolean isSent) {
    this.topic = topic;
    this.message = message;
    this.isSent = false;
  }

  public void isSent() {
    this.isSent = true;
  }

}
