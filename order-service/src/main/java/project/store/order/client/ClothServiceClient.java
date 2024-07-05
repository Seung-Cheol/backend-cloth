package project.store.order.client;


import org.springframework.cloud.openfeign.FeignClient;
import project.store.order.common.CommonResponseDto;

@FeignClient(name = "member-service")
public interface ClothServiceClient {
  CommonResponseDto<?> updateInventory(Long clothDetailId, int quantity);
}
