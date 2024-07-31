package project.store.order.service;


import com.netflix.discovery.converters.Auto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.store.order.api.dto.request.WishListAddRequestDto;
import project.store.order.api.dto.request.WishListUpdateRequestDto;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.client.ClothServiceClient;
import project.store.order.common.CommonResponseDto;
import project.store.order.domain.entity.WishList;
import project.store.order.domain.repository.WishListRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WishListService {

  private final WishListRepository wishListRepository;
  private final ClothServiceClient clothServiceClient;

  public Mono<Void> addWishList(Long memberId, WishListAddRequestDto wishListAddRequestDto) {
    WishList wishList = WishList.builder()
      .wishlistClothCount(wishListAddRequestDto.getQuantity())
      .clothDetailId(wishListAddRequestDto.getClothDetailId())
      .memberId(memberId)
      .build();

    return wishListRepository.save(wishList).then();
  }

  public Flux<WishListResponseDto> getWishList(Long memberId) {
    return wishListRepository.findAllByMemberId(memberId)
      .flatMap(wishList -> {
        List<Long> ids = List.of(wishList.getClothDetailId());
        return clothServiceClient.getClothDetails(ids)
          .flatMapMany(clothDetails -> {
            ClothDetailResponseDto clothDetailResponseDto = clothDetails.getData().stream()
              .filter(clothDetail -> clothDetail.getClothDetailId().equals(wishList.getClothDetailId()))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
            return Mono.just(WishListResponseDto.builder()
              .id(wishList.getId())
              .clothDetailId(wishList.getClothDetailId())
              .wishlistClothCount(wishList.getWishlistClothCount())
              .clothDetailResponseDto(clothDetailResponseDto)
              .build());
          });
      });
  }

  public Flux<WishList> getWishListByIds(Long[] wishListIds) {
    return wishListRepository.findByIdIn(List.of(wishListIds));
  }

  public Mono<Void> updateWishList(WishListUpdateRequestDto wishListUpdateRequestDto, Long memberId) {
    return wishListRepository.findByIdAndMemberId(wishListUpdateRequestDto.getWishListId(), memberId)
      .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 위시리스트가 없습니다.")))
      .doOnNext(wishList -> wishList.updateWishList(wishListUpdateRequestDto.getQuantity()))
      .flatMap(wishListRepository::save)
      .then();
  }

  public Mono<Void> deleteWishList(Long wishListId, Long memberId) {
    return wishListRepository.findByIdAndMemberId(wishListId, memberId)
      .switchIfEmpty(Mono.error(new IllegalArgumentException("해당 위시리스트가 없습니다.")))
      .flatMap(wishListRepository::delete)
      .then();
  }

  public Mono<Void> deleteWishListByIds(Long[] wishListIds) {
    return wishListRepository.findByIdIn(List.of(wishListIds))
      .flatMap(wishListRepository::delete)
      .then();
  }
}
