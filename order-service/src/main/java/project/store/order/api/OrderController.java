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

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderFacade orderFacade;
  private final OrderService orderService;

  @PostMapping("/add")
  public CommonResponseDto<String> addOrder(
    @RequestHeader("id") Long memberId,
    @RequestBody OrderRequestDto dto) {
    orderFacade.orderFromDetail(dto, memberId);
    return new CommonResponseDto<>();
  }

  @PostMapping("/add/WishList")
  public CommonResponseDto<String> addOrderFromWishList(
    @RequestHeader("id") Long memberId,
    OrderFromWishListRequestDto dto) {
    orderFacade.orderFromWishList(dto, memberId);
    return new CommonResponseDto<>();
  }


  @PutMapping("cancel")
  public CommonResponseDto<?> cancelOrder(
    Long orderId, @RequestHeader("id") Long memberId){
    orderFacade.cancelOrder(orderId,memberId);
    return new CommonResponseDto<>();
  }

  @PutMapping("refund")
  public CommonResponseDto<?> refundOrder(
    Long orderId, @RequestHeader("id") Long memberId
  ) {
    orderFacade.refundOrder(orderId, memberId);
    return new CommonResponseDto<>();
  }


  @GetMapping("/detail/{orderId}")
  public CommonResponseDto<Order> getOrderDetail(
    @PathVariable Long orderId) {
    Order data = orderService.getOrderById(orderId);
    return CommonResponseDto.ofSuccess(data);
  }

  @GetMapping("/list/{memberId}")
  public CommonResponseDto<List<OrderListResponseDto>> getOrderList(@PathVariable Long memberId) {
    List<OrderListResponseDto> data = orderService.getOrderList(memberId);
    return CommonResponseDto.ofSuccess(data);
  }

}
