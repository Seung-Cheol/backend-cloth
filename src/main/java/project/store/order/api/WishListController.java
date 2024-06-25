package project.store.order.api;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.member.api.dto.response.CommonResponseDto;

@RestController
@RequestMapping("wishlist")
public class WishListController {

  @PostMapping("/add")
  public CommonResponseDto<String> addWishList() {
    return CommonResponseDto.ofSuccess("성공", "찜목록 추가 성공");
  }

  @GetMapping("/list")
  public CommonResponseDto<String> getWishList() {
    return CommonResponseDto.ofSuccess("성공", "찜목록 조회 성공");
  }

  @PutMapping("/update")
  public CommonResponseDto<String> updateWishList() {
    return CommonResponseDto.ofSuccess("성공", "찜목록 수정 성공");
  }

  @DeleteMapping("/delete")
  public CommonResponseDto<String> deleteWishList() {
    return CommonResponseDto.ofSuccess("성공", "찜목록 삭제 성공");
  }

}
