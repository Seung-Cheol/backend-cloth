package project.store.cloth.api.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothPicture;
import project.store.cloth.domain.ClothType;

@Data
@Builder
public class ClothDetailResponseDto {


  private Long id;
  private String clothName;
  private String clothContent;
  private String thumbnail;
  private int price;
  private LocalDate created_at;
  private ClothType clothType;
  private List<ClothDetail> clothDetails;
  private List<ClothPicture> clothPictures;

  public static ClothDetailResponseDto toDto(Cloth cloth) {
    if (cloth == null) {
      throw new IllegalArgumentException("옷 정보가 없습니다.");
    }

    return ClothDetailResponseDto.builder()
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
