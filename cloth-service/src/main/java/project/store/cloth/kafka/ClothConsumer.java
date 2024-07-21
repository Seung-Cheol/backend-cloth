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
import project.store.cloth.lock.DistributedLock;
import project.store.cloth.service.ClothService;

@Component
@RequiredArgsConstructor
public class ClothConsumer {

  private final ClothDetailRepository clothDetailRepository;
  private final ClothOutboxRepository clothOutboxRepository;
  private final ClothService clothService;
  private final ClothProducer clothProducer;

  @KafkaListener(topics = "order_complete")
  public void consumeOrderComplete(String message) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
      String json = mapper.readValue(message, String.class);
      List<Map<Object, Object>> list = mapper.readValue(json, new TypeReference<List<Map<Object,Object>>>() {});
      list.forEach(
        map -> {
          Long clothDetailId = Long.parseLong(map.get("clothDetailId").toString());
          Long orderId = Long.parseLong(map.get("orderId").toString());
          int quantity = Integer.parseInt(map.get("quantity").toString());
          clothService.updateInventory(orderId, clothDetailId, quantity);
    });



  }
}
