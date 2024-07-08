package project.store.order.client;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.common.CommonResponseDto;

@FeignClient(name = "cloth-service")
@Component
public interface ClothServiceClient {

  @GetMapping("/cloth/detail")
  CommonResponseDto<List<ClothDetailResponseDto>> getClothDetails(@RequestBody List<Long> clothDetailIds);

}
