package project.store.client;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cloth-service")
public class ClothClientService {

  @GetMapping("/cloth/inventory")
  public int getInventory(@RequestBody List<Integer> clothDetailIds) {
    return 0;
  }

}
