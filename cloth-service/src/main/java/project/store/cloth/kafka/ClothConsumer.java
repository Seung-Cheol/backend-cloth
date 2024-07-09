package project.store.cloth.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothOutbox;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.cloth.domain.repository.ClothOutboxRepository;
import project.store.cloth.service.ClothService;

@Component
@RequiredArgsConstructor
public class ClothConsumer {

  private final ClothDetailRepository clothDetailRepository;
  private final ClothOutboxRepository clothOutboxRepository;
  private final ClothProducer clothProducer;

  @KafkaListener(topics = "order_complete")
  public void consumeOrderComplete(String message) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
      String json = mapper.readValue(message, String.class);
      List<Map<Object, Object>> list = mapper.readValue(json, new TypeReference<List<Map<Object,Object>>>() {});
      list.forEach(
        map -> {
          Long clothDetailId = Long.parseLong(map.get("clothDetailId").toString());
          int quantity = Integer.parseInt(map.get("quantity").toString());
          try {
            ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId)
              .orElseThrow(() -> new RuntimeException("Cloth detail not found for ID: " + clothDetailId));
            clothDetail.updateInventory(clothDetail.getInventory() - quantity);
            ClothDetail savedClothDetail = clothDetailRepository.save(clothDetail);
            if (savedClothDetail == null) {
              throw new RuntimeException("Failed to save cloth detail for ID: " + clothDetailId);
            }
          } catch (Exception e) {
            clothOutboxRepository.save(ClothOutbox.builder()
              .topic("order_rollback")
              .message(map.get("orderId").toString())
              .isSent(false)
              .build());
            clothOutboxRepository.save(ClothOutbox.builder()
              .topic("payment_rollback")
              .message(map.get("orderId").toString())
              .isSent(false)
              .build());
            clothProducer.send();
          }
    });



  }
}
