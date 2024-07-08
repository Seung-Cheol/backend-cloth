package project.store.cloth.api.dto.response;


import lombok.Builder;
import lombok.Data;
import project.store.cloth.domain.ClothDetail;

@Data
@Builder
public class ClothDetailResponseDto {

  private Long clothDetailId;

  private String thumbnail;

  private String size;

  private int price;

  private String clothName;

  public static ClothDetailResponseDto toDto(ClothDetail clothDetail) {
    if (clothDetail == null) {
      throw new IllegalArgumentException("옷 정보가 없습니다.");
    }

    return ClothDetailResponseDto.builder()
      .clothDetailId(clothDetail.getId())
      .thumbnail(clothDetail.getCloth().getThumbnail())
      .size(String.valueOf(clothDetail.getSize()))
      .price(clothDetail.getCloth().getPrice())
      .clothName(clothDetail.getCloth().getClothName())
      .build();
  }
}
