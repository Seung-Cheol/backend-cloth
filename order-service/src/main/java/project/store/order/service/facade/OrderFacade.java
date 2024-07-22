package project.store.order.service.facade;


import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.client.ClothServiceClient;
import project.store.order.common.exception.CustomException;
import project.store.order.common.exception.OrderExceptionEnum;
import project.store.order.common.util.RedisUtil;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.entity.WishList;
import project.store.order.lock.DistributedLock;
import project.store.order.service.OrderService;
import project.store.order.service.WishListService;
import project.store.order.service.dto.OrderClothDto;
import project.store.order.service.dto.OrderDto;

@RequiredArgsConstructor
@Component
public class OrderFacade {
  private final WishListService wishListService;
  private final OrderService orderService;
  private final ClothServiceClient clothServiceClient;
  private final RedisUtil redisUtil;


  @Transactional
  public void orderFromWishList(OrderFromWishListRequestDto dto, Long memberId) {

    // 주문 생성
    OrderDto orderInfo = OrderDto.builder()
      .orderAddress(dto.getOrderAddress())
      .memberId(memberId)
      .orderStatus(OrderStatus.INITIATED)
      .build();
    Order order = orderService.createOrder(orderInfo);

    //카트에서 상품 정보 추출
    List<WishList> wishLists = wishListService.getWishListByIds(dto.getWishListIds());

    //주문 상세 넣는다 (OrderClothTable)
    List<OrderClothDto> orderClothDtos = wishLists.stream()
      .map(wishList -> OrderClothDto.builder()
        .quantity(wishList.getWishlistClothCount())
        .order(order)
        .clothDetailId(wishList.getClothDetailId())
        .build())
      .toList();
    orderService.createOrderCloth(orderClothDtos);

  }

  @Transactional
  @DistributedLock(key = "#dto.clothDetailId")
  public void orderFromDetail(OrderRequestDto dto, Long memberId) {
    //재고 차감
    boolean result = redisUtil.decreaseStock(String.valueOf(dto.getClothDetailId()), dto.getQuantity());
    if (!result) {
      throw new CustomException(OrderExceptionEnum.CLOTH_INVENTORY_NOT_ENOUGH);
    }

    //주문 생성
    OrderDto orderInfo = OrderDto.builder()
      .orderAddress(dto.getOrderAddress())
      .memberId(memberId)
      .orderStatus(OrderStatus.INITIATED)
      .build();
    Order order = orderService.createOrder(orderInfo);

    //주문 상세 넣는다 (OrderClothTable)
    OrderClothDto orderClothDto = OrderClothDto.builder()
      .quantity(dto.getQuantity())
      .order(order)
      .clothDetailId(dto.getClothDetailId())
      .build();
    orderService.createOrderCloth(new ArrayList<>(List.of(orderClothDto)));

  }


  @Transactional
  public String cancelOrder(Long orderId, Long memberId) {

    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.PAID) {
      return "취소는 배송 전에만 가능합니다.";
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.CANCEL);

    //Order Detail 갯수 가져오고 재고증가

    return "주문이 취소되었습니다.";
  }

  @Transactional
  public String refundOrder(Long orderId, Long memberId) {
    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.COMPLETE) {
      return "환불은 배송 후 가능합니다.";
    }

    if (LocalDateTime.now().isBefore(order.getDeliveryDate().plusDays(1))) {
      return "환불은 배송 완료 후 1일 이내에만 가능합니다.";
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.REFUND);


    return "주문이 환불되었습니다.";
  }



}
