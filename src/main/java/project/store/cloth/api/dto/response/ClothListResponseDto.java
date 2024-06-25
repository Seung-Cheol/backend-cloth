package project.store.cloth.api.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.store.cloth.domain.ClothType;


@Data
@Builder
public class ClothListResponseDto {

  private Long id;
  private String clothName;
  private String thumbnail;
  private int price;
  private ClothType clothType;

  public ClothListResponseDto(Long id, String clothName, String thumbnail, int price, ClothType clothType) {
    this.id = id;
    this.clothName = clothName;
    this.thumbnail = thumbnail;
    this.price = price;
    this.clothType = clothType;
  }


}
