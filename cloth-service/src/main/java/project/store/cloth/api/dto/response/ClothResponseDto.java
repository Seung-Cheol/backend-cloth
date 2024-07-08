package project.store.cloth.api.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothPicture;
import project.store.cloth.domain.ClothType;

@Data
@Builder
public class ClothResponseDto {


  private Long id;
  private String clothName;
  private String clothContent;
  private String thumbnail;
  private int price;
  private LocalDate created_at;
  private ClothType clothType;
  private List<ClothDetail> clothDetails;
  private List<ClothPicture> clothPictures;

  public static ClothResponseDto toDto(Cloth cloth) {
    if (cloth == null) {
      throw new IllegalArgumentException("옷 정보가 없습니다.");
    }

    return ClothResponseDto.builder()
        .id(cloth.getId())
        .clothName(cloth.getClothName())
        .clothContent(cloth.getClothContent())
        .thumbnail(cloth.getThumbnail())
        .price(cloth.getPrice())
        .created_at(cloth.getCreated_at())
        .clothType(cloth.getClothType())
        .clothDetails(cloth.getClothDetails())
        .clothPictures(cloth.getClothPictures())
        .build();
  }
}
