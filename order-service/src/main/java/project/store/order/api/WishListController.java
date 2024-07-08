package project.store.order.api;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.order.api.dto.request.WishListAddRequestDto;
import project.store.order.api.dto.request.WishListUpdateRequestDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.common.CommonResponseDto;
import project.store.order.service.WishListService;

@RestController
@RequestMapping("wishlist")
@RequiredArgsConstructor
public class WishListController {

  private final WishListService wishListService;

  @PostMapping("/add")
  public CommonResponseDto<String> addWishList(
    @RequestHeader("id") Long memberId,
    @Valid @RequestBody WishListAddRequestDto wishListAddRequestDto) {
    wishListService.addWishList(memberId, wishListAddRequestDto);
    return new CommonResponseDto<>();
  }

  @GetMapping("/list")
  public CommonResponseDto<List<WishListResponseDto>> getWishList(
    @RequestHeader("id") Long memberId) {
    List<WishListResponseDto> data = wishListService.getWishList(memberId);
    return CommonResponseDto.ofSuccess(data);
  }

  @PutMapping("/update")
  public CommonResponseDto<?> updateWishList(
    @RequestHeader("id") Long memberId,
    @RequestBody WishListUpdateRequestDto wishListUpdateRequestDto) {
    wishListService.updateWishList(wishListUpdateRequestDto, memberId);
    return new CommonResponseDto<>();
  }

  @DeleteMapping("/delete")
  public CommonResponseDto<String> deleteWishList(
    @RequestHeader("id") Long memberId, Long WishListId) {
    wishListService.deleteWishList(WishListId, memberId);
    return new CommonResponseDto<>();
  }

}
