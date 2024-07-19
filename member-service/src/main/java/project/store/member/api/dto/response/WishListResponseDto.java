package project.store.member.api.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishListResponseDto {

  private Long id;

  private Long clothDetailId;

  private int wishlistClothCount;

  private ClothDetailResponseDto clothDetailResponseDto;
}
