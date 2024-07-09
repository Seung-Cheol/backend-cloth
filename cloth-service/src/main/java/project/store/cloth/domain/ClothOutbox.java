package project.store.cloth.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;

@Entity
@Builder
@Getter
@NoArgsConstructor
public class ClothOutbox {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String topic;
  private String message;
  private boolean isSent;

  public ClothOutbox(Long id,String topic, String message, boolean isSent) {
    this.topic = topic;
    this.message = message;
    this.isSent = false;
  }

  public void isSent() {
    this.isSent = true;
  }


}
