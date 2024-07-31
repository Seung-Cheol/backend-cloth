package project.store.order.service.facade;


import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.client.ClientOrderRequestDto;
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
  private final PlatformTransactionManager tx;
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


  @DistributedLock(key = "#dto.clothDetailId")
  public void orderFromDetail(OrderRequestDto dto, Long memberId) {
    TransactionDefinition def = new DefaultTransactionDefinition();
    TransactionStatus status = tx.getTransaction(def);

    //재고 차감
    boolean result = redisUtil.decreaseStock(String.valueOf(dto.getClothDetailId()), dto.getQuantity());
    if (!result) {
      throw new CustomException(OrderExceptionEnum.CLOTH_INVENTORY_NOT_ENOUGH);
    }

    try {

      // 주문 생성
      OrderDto orderInfo = OrderDto.builder()
        .orderAddress(dto.getOrderAddress())
        .memberId(memberId)
        .orderStatus(OrderStatus.INITIATED)
        .build();
      Order order = orderService.createOrder(orderInfo);

      // 주문 상세 넣는다 (OrderClothTable)
      OrderClothDto orderClothDto = OrderClothDto.builder()
        .quantity(dto.getQuantity())
        .order(order)
        .clothDetailId(dto.getClothDetailId())
        .build();
      orderService.createOrderCloth(new ArrayList<>(List.of(orderClothDto)));

      // 트랜잭션 커밋
      tx.commit(status);
    } catch (Exception e) {
      e.printStackTrace();
      try {
        if (tx != null) {
          redisUtil.increaseStock(String.valueOf(dto.getClothDetailId()), dto.getQuantity());
          tx.rollback(status);
        }
      } catch (Exception ex) {
        e.printStackTrace();
        throw new CustomException(OrderExceptionEnum.ORDER_FAILED);
      }
      throw new CustomException(OrderExceptionEnum.ORDER_FAILED);
    }
  }


  @Transactional
  public void cancelOrder(Long orderId, Long memberId) {

    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.PAID) {
      throw new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED);
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.CANCEL);

  }

  @Transactional
  public void refundOrder(Long orderId, Long memberId) {
    Order order = orderService.getOrderById(orderId);
    if(order.getOrderStatus()!=OrderStatus.COMPLETE) {
      throw new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED);
    }

    if (LocalDateTime.now().isBefore(order.getDeliveryDate().plusDays(1))) {
      throw new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED);
    }

    //주문 상태 변경
    orderService.updateOrderStatus(orderId, OrderStatus.REFUND);

  }



}
