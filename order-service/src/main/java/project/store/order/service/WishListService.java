package project.store.order.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.store.order.api.dto.request.WishListAddRequestDto;
import project.store.order.api.dto.request.WishListUpdateRequestDto;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.client.ClothServiceClient;
import project.store.order.common.CommonResponseDto;
import project.store.order.domain.entity.WishList;
import project.store.order.domain.repository.WishListRepository;

@Service
@RequiredArgsConstructor
public class WishListService {

  private final WishListRepository wishListRepository;
  private final ClothServiceClient clothServiceClient;

  public void addWishList(Long memberId, WishListAddRequestDto wishListAddRequestDto) {

    WishList wishList = WishList.builder()
      .wishlistClothCount(wishListAddRequestDto.getQuantity())
      .clothDetailId(wishListAddRequestDto.getClothDetailId())
      .memberId(memberId)
      .build();

    wishListRepository.save(wishList);

  }

  public List<WishListResponseDto> getWishList(Long memberId) {
    List<WishList> list = wishListRepository.findAllByMemberId(memberId);
    List<Long> ids = list.stream().map(WishList::getClothDetailId).toList();
    CommonResponseDto<List<ClothDetailResponseDto>> clothDetails = clothServiceClient.getClothDetails(ids);
    List<WishListResponseDto> data = list.stream().map(wishList -> {
      ClothDetailResponseDto clothDetailResponseDto = clothDetails.getData().stream()
        .filter(clothDetail -> clothDetail.getClothDetailId().equals(wishList.getClothDetailId()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
      return WishListResponseDto.builder()
        .id(wishList.getId())
        .clothDetailId(wishList.getClothDetailId())
        .wishlistClothCount(wishList.getWishlistClothCount())
        .clothDetailResponseDto(clothDetailResponseDto)
        .build();
    }).toList();

    return data;
  }

  public List<WishList> getWishListByIds(Long[] wishListIds) {
    return wishListRepository.findByIds(wishListIds);
  }

  public void updateWishList(WishListUpdateRequestDto wishListUpdateRequestDto, Long memberId) {
    WishList wishList = wishListRepository.findByIdAndMemberId(wishListUpdateRequestDto.getWishListId(), memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다."));
    wishList.updateWishList(wishListUpdateRequestDto.getQuantity());
    wishListRepository.save(wishList);
  }

  public void deleteWishList(Long wishListId, Long memberId) {
    WishList wishList = wishListRepository.findByIdAndMemberId(wishListId, memberId)
      .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다."));
    wishListRepository.delete(wishList);
  }

  public void deleteWishListByIds(Long[] wishListIds) {
    List<WishList> wishLists = wishListRepository.findByIds(wishListIds);
    wishListRepository.deleteAll(wishLists);
  }
}
