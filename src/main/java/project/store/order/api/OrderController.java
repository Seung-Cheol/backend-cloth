package project.store.order.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.member.api.dto.response.CommonResponseDto;

@RestController
@RequestMapping("order")
public class OrderController {

  @PostMapping("/add")
  public CommonResponseDto<String> addOrder() {
    return CommonResponseDto.ofData("성공", "주문 추가 성공");
  }

  @PostMapping("/add/WishList")
  public CommonResponseDto<String> addOrderFromWishList() {
    return CommonResponseDto.ofData("성공", "주문 추가 성공");
  }

  @GetMapping("/list")
  public CommonResponseDto<String> getOrderList() {
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
