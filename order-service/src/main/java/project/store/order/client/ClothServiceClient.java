package project.store.order.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.common.CommonResponseDto;

@FeignClient(name = "cloth-service")
@Component
public interface ClothServiceClient {

  @PostMapping("/cloth/details")
  @CircuitBreaker(name="circuitBreaker", fallbackMethod = "getClothDetailsFallback")
  @Retry(name="retry")
  CommonResponseDto<List<ClothDetailResponseDto>> getClothDetails(@RequestBody List<Long> clothDetailIds);

  @PostMapping("/cloth/inventory")
  @CircuitBreaker(name="circuitBreaker")
  @Retry(name="retry")
  CommonResponseDto<Map<Long, Integer>> getInventory(@RequestBody List<Long> clothDetailIds);

  @PutMapping("/cloth/inventory")
  @CircuitBreaker(name="circuitBreaker")
  @Retry(name="retry")
  CommonResponseDto<?> updateInventory(@RequestBody ClientOrderRequestDto orderRequestDto);

  default CommonResponseDto<?> getClothDetailsFallback(List<Long> clothDetailIds, Throwable t) {
    List<ClothDetailResponseDto> arr = List.of();
    return CommonResponseDto.ofSuccess(arr);
  }


}
