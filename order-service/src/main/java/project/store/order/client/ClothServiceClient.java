package project.store.order.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.common.CommonResponseDto;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClothServiceClient {

  private final WebClient webClient;

  @CircuitBreaker(name="circuitBreaker", fallbackMethod = "getClothDetailsFallback")
  @Retry(name="retry")
  public Mono<CommonResponseDto<List<ClothDetailResponseDto>>> getClothDetails(List<Long> clothDetailIds) {
    return webClient.post()
      .uri("/cloth/details")
      .bodyValue(clothDetailIds)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<CommonResponseDto<List<ClothDetailResponseDto>>>() {});
  }

  @CircuitBreaker(name="circuitBreaker")
  @Retry(name="retry")
  public Mono<CommonResponseDto<Map<Long, Integer>>> getInventory(List<Long> clothDetailIds) {
    return webClient.post()
      .uri("/cloth/inventory")
      .bodyValue(clothDetailIds)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<CommonResponseDto<Map<Long, Integer>>>() {});
  }

  @CircuitBreaker(name="circuitBreaker")
  @Retry(name="retry")
  public Mono<CommonResponseDto<?>> updateInventory(ClientOrderRequestDto orderRequestDto) {
    return webClient.put()
      .uri("/cloth/inventory")
      .bodyValue(orderRequestDto)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<CommonResponseDto<?>>() {});
  }

  public Mono<CommonResponseDto<List<ClothDetailResponseDto>>> getClothDetailsFallback(List<Long> clothDetailIds, Throwable t) {
    List<ClothDetailResponseDto> arr = List.of();
    return Mono.just(CommonResponseDto.ofSuccess(arr));
  }
}
