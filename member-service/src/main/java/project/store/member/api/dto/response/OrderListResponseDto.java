package project.store.member.api.dto.response;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseDto {

  private Long orderId;

  private OrderStatus orderStatus;

  private String address;

  private String addressDetail;

  private LocalDateTime orderDate;

  private LocalDateTime deliveryDate;

  private int totalPrice;

  private List<ClothDetailResponseDto> clothDetailResponseDtos;
}

enum OrderStatus {
  INITIATED,PAID,DELIVERY,COMPLETE,REFUND,CANCEL
}

