package project.store.order.api;


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
import project.store.order.common.CommonResponseDto;
import project.store.order.domain.entity.Order;
import project.store.order.service.OrderService;
import project.store.order.service.usecase.OrderUseCase;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderUseCase orderUseCase;
  private final OrderService orderService;

  @PostMapping("/add")
  public CommonResponseDto<String> addOrder(
    @RequestHeader("id") Long memberId,
    @RequestBody OrderRequestDto dto) {
    orderUseCase.orderFromDetail(dto, memberId);
    return new CommonResponseDto<>();
  }

  @PostMapping("/add/WishList")
  public CommonResponseDto<String> addOrderFromWishList(
    @RequestHeader("id") Long memberId,
    OrderFromWishListRequestDto dto) {
    String message = orderUseCase.orderFromWishList(dto, memberId);
    return new CommonResponseDto<>();
  }

  @GetMapping("/list")
  public CommonResponseDto<Order> getOrderList(@RequestHeader("id") Long memberId) {
    Order data = orderService.orderByMemberId(memberId);
    return CommonResponseDto.ofSuccess(data);
  }

  @PutMapping("cancel")
  public CommonResponseDto<?> cancelOrder(
    Long orderId, @RequestHeader("id") Long memberId){
    orderUseCase.cancelOrder(orderId,memberId);
    return new CommonResponseDto<>();
  }

  @PutMapping("refund")
  public CommonResponseDto<?> refundOrder(
    Long orderId, @RequestHeader("id") Long memberId
  ) {
    orderUseCase.refundOrder(orderId, memberId);
    return new CommonResponseDto<>();
  }


  @GetMapping("/detail/{orderId}")
  public CommonResponseDto<Order> getOrderDetail(
    @PathVariable Long orderId) {
    Order data = orderService.getOrderById(orderId);
    return CommonResponseDto.ofSuccess(data);
  }

}
