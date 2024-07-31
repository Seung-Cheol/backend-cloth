package project.store.order.api;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
import project.store.order.api.dto.response.OrderListResponseDto;
import project.store.order.common.CommonResponseDto;
import project.store.order.domain.entity.Order;
import project.store.order.service.OrderService;
import project.store.order.service.facade.OrderFacade;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderFacade orderFacade;
  private final OrderService orderService;

  @PostMapping("/add")
  public Mono<CommonResponseDto<?>> addOrder(
    @RequestHeader("id") Long memberId,
    @RequestBody OrderRequestDto dto) {
    return Mono.fromCallable(() -> {
      orderFacade.orderFromDetail(dto, memberId);
      return new CommonResponseDto<>();
    });
  }

  @PostMapping("/add/WishList")
  public Mono<CommonResponseDto<String>> addOrderFromWishList(
    @RequestHeader("id") Long memberId,
    OrderFromWishListRequestDto dto) {
    return Mono.fromCallable(() -> {
      orderFacade.orderFromWishList(dto, memberId);
      return new CommonResponseDto<>();
    });
  }

  @PutMapping("cancel")
  public Mono<CommonResponseDto<?>> cancelOrder(
    Long orderId, @RequestHeader("id") Long memberId) {
    return Mono.fromCallable(() -> {
      orderFacade.cancelOrder(orderId, memberId);
      return new CommonResponseDto<>();
    });
  }

  @PutMapping("refund")
  public Mono<CommonResponseDto<?>> refundOrder(
    Long orderId, @RequestHeader("id") Long memberId) {
    return Mono.fromCallable(() -> {
      orderFacade.refundOrder(orderId, memberId);
      return new CommonResponseDto<>();
    });
  }

  @GetMapping("/detail/{orderId}")
  public Mono<CommonResponseDto<Order>> getOrderDetail(
    @PathVariable Long orderId) {
    return orderService.getOrderById(orderId)
      .map(CommonResponseDto::ofSuccess);
  }

  @GetMapping("/list/{memberId}")
  public Flux<CommonResponseDto<OrderListResponseDto>> getOrderList(@PathVariable Long memberId) {
    return orderService.getOrderList(memberId)
      .map(CommonResponseDto::ofSuccess);
  }
}
