package project.store.order.api;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.order.api.dto.request.WishListAddRequestDto;
import project.store.order.api.dto.request.WishListUpdateRequestDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.common.CommonResponseDto;
import project.store.order.common.CustomMember;
import project.store.order.service.WishListService;

@RestController
@RequestMapping("wishlist")
@RequiredArgsConstructor
public class WishListController {

  private final WishListService wishListService;

  @PostMapping("/add")
  public CommonResponseDto<String> addWishList(
    @AuthenticationPrincipal CustomMember customMember,
    @Valid @RequestBody WishListAddRequestDto wishListAddRequestDto) {
    String message = wishListService.addWishList(customMember.getId(),wishListAddRequestDto);
    return CommonResponseDto.of(message);
  }

  @GetMapping("/list")
  public CommonResponseDto<List<WishListResponseDto>> getWishList(
    @AuthenticationPrincipal CustomMember customMember) {
    List<WishListResponseDto> data = wishListService.getWishList(customMember.getId());
    return CommonResponseDto.ofData("성공", data);
  }

  @PutMapping("/update")
  public CommonResponseDto<String> updateWishList(
    @AuthenticationPrincipal CustomMember customMember,
    @RequestBody WishListUpdateRequestDto wishListUpdateRequestDto) {
    String message = wishListService.updateWishList(wishListUpdateRequestDto, customMember.getId());
    return CommonResponseDto.of(message);
  }

  @DeleteMapping("/delete")
  public CommonResponseDto<String> deleteWishList(
    @AuthenticationPrincipal CustomMember customMember, Long WishListId) {
    String message = wishListService.deleteWishList(WishListId, customMember.getId());
    return CommonResponseDto.of(message);
  }

}
