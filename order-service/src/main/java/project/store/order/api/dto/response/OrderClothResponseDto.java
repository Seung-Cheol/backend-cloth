package project.store.order.api.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderClothResponseDto {
    private Long clothDetailId;

    private String thumbnail;

    private String size;

    private int price;

    private String clothName;
}

