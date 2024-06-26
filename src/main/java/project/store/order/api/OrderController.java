package project.store.order.api;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.member.api.dto.response.CommonResponseDto;
import project.store.member.auth.CustomMember;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
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
    @AuthenticationPrincipal CustomMember customMember,
    OrderRequestDto dto) {
    orderUseCase.orderFromDetail(dto, customMember.getId());
    return CommonResponseDto.ofData("성공", "주문 추가 성공");
  }

  @PostMapping("/add/WishList")
  public CommonResponseDto<String> addOrderFromWishList(
    @AuthenticationPrincipal CustomMember customMember,
    OrderFromWishListRequestDto dto) {
    String message = orderUseCase.orderFromWishList(dto,customMember.getId());
    return CommonResponseDto.of(message);
  }

  @GetMapping("/list")
  public CommonResponseDto<String> getOrderList(@AuthenticationPrincipal CustomMember customMember) {
//    orderService.getOrderList(customMember.getId());
    return CommonResponseDto.ofData("성공", "주문 조회 성공");
  }

  @PutMapping("cancel")
  public CommonResponseDto<String> cancelOrder() {
    return CommonResponseDto.ofData("성공", "주문 취소 성공");
  }

  @PutMapping("refund")
  public CommonResponseDto<String> refundOrder() {
    return CommonResponseDto.ofData("성공", "주문 환불 성공");
  }

}
