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

  public ClothDetailResponseDto(Long id, String clothName, String clothContent, String thumbnail, int price, LocalDate created_at, ClothType clothType, List<ClothDetail> clothDetails, List<ClothPicture> clothPictures) {
    this.id = id;
    this.clothName = clothName;
    this.clothContent = clothContent;
    this.thumbnail = thumbnail;
    this.price = price;
    this.created_at = created_at;
    this.clothType = clothType;
    this.clothDetails = clothDetails;
    this.clothPictures = clothPictures;
  }
}
