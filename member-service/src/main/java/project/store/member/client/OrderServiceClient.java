package project.store.member.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.store.member.api.dto.response.ClothDetailResponseDto;
import project.store.member.api.dto.response.OrderListResponseDto;
import project.store.member.api.dto.response.WishListResponseDto;
import project.store.member.common.CommonResponseDto;

@Component
@FeignClient(name = "order-service")
@CircuitBreaker(name="circuitBreaker")
@Retry(name="retry")
public interface OrderServiceClient {

  @GetMapping("/order/list/{memberId}")
  CommonResponseDto<List<OrderListResponseDto>> getOrderList(@PathVariable Long memberId);

  @GetMapping("/wishlist/list/{memberId}")
  CommonResponseDto<List<WishListResponseDto>> getWishList(@PathVariable Long memberId);

}
