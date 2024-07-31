package project.store.order.api;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("wishlist")
@RequiredArgsConstructor
public class WishListController {

  private final WishListService wishListService;

  @PostMapping("/add")
  public Mono<CommonResponseDto<String>> addWishList(
    @RequestHeader("id") Long memberId,
    @Valid @RequestBody WishListAddRequestDto wishListAddRequestDto) {
    return wishListService.addWishList(memberId, wishListAddRequestDto)
      .thenReturn(new CommonResponseDto<>());
  }

  @GetMapping("/list/{memberId}")
  public Mono<CommonResponseDto<List<WishListResponseDto>>> getWishList(
    @PathVariable Long memberId) {
    return wishListService.getWishList(memberId)
      .collectList()
      .map(CommonResponseDto::ofSuccess);
  }

  @PutMapping("/update")
  public Mono<CommonResponseDto<?>> updateWishList(
    @RequestHeader("id") Long memberId,
    @RequestBody WishListUpdateRequestDto wishListUpdateRequestDto) {
    return wishListService.updateWishList(wishListUpdateRequestDto, memberId)
      .thenReturn(new CommonResponseDto<>());
  }

  @DeleteMapping("/delete")
  public Mono<CommonResponseDto<String>> deleteWishList(
    @RequestHeader("id") Long memberId, Long wishListId) {
    return wishListService.deleteWishList(wishListId, memberId)
      .thenReturn(new CommonResponseDto<>());
  }
}
