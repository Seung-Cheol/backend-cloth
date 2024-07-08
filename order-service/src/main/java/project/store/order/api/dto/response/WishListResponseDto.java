package project.store.order.api.dto.response;


import lombok.Builder;
import lombok.Data;
import project.store.order.domain.entity.WishList;

@Data
@Builder
public class WishListResponseDto {

  private Long id;

  private Long clothDetailId;

  private int wishlistClothCount;

  private ClothDetailResponseDto clothDetailResponseDto;
}
