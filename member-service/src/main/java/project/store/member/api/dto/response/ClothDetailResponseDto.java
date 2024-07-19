package project.store.member.api.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClothDetailResponseDto {

  private Long clothDetailId;

  private String thumbnail;

  private String size;

  private int price;

  private String clothName;
}
