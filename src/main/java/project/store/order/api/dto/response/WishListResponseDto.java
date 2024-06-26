package project.store.order.api.dto.response;


import lombok.Builder;
import lombok.Data;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothSize;
import project.store.order.domain.entity.WishList;

@Data
@Builder
public class WishListResponseDto {

  private Long id;

  private Long clothDetailId;

  private int wishlistClothCount;

  private String thumbnail;

  private ClothSize size;

  private int price;

  private String clothName;

  public static WishListResponseDto toDto(WishList wishList) {
    if (wishList == null) {
      throw new IllegalArgumentException("위시리스트 정보가 없습니다.");
    }

    ClothDetail clothDetail = wishList.getClothDetail();
    if (clothDetail == null) {
      throw new IllegalArgumentException("옷 디테일 정보가 없습니다");
    }

    Cloth cloth = clothDetail.getCloth();
    if (cloth == null) {
      throw new IllegalArgumentException("옷 정보가 없습니다");
    }

    return WishListResponseDto.builder()
        .id(wishList.getId())
        .clothDetailId(wishList.getClothDetail().getId())
        .wishlistClothCount(wishList.getWishlistClothCount())
        .thumbnail(wishList.getClothDetail().getCloth().getThumbnail())
        .size(wishList.getClothDetail().getSize())
        .price(wishList.getClothDetail().getCloth().getPrice())
        .clothName(wishList.getClothDetail().getCloth().getClothName())
        .build();
  }
}
