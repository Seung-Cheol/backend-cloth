package project.store.order.api.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.store.order.domain.entity.OrderAddress;
import project.store.order.domain.entity.OrderStatus;

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
