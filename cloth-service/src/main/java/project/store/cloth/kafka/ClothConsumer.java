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
import project.store.cloth.service.ClothService;

@Component
@RequiredArgsConstructor
public class ClothConsumer {

  private final ClothService clothService;

  @KafkaListener(topics = "order_complete")
  public void consumeOrderComplete(String message) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.readValue(message, String.class);
      List<Map<Object, Object>> list = mapper.readValue(json, new TypeReference<List<Map<Object,Object>>>() {});
      list.forEach(
        map -> clothService.updateInventory(Long.parseLong(map.get("clothDetailId").toString()),
          -Integer.parseInt(map.get("quantity").toString())));

    } catch (JsonProcessingException e) {

    }

  }
}
