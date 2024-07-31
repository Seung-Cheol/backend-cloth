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
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import project.store.order.api.dto.request.OrderFromWishListRequestDto;
import project.store.order.api.dto.request.OrderRequestDto;
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
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class OrderFacade {
  private final WishListService wishListService;
  private final OrderService orderService;
  private final ClothServiceClient clothServiceClient;
  private final ReactiveTransactionManager reactiveTransactionManager;
  private final RedisUtil redisUtil;

  @DistributedLock(key = "#dto.wishListIds")
  public Mono<Void> orderFromWishList(OrderFromWishListRequestDto dto, Long memberId) {
    TransactionalOperator txOperator = TransactionalOperator.create(reactiveTransactionManager);

    // 주문 생성
    OrderDto orderInfo = OrderDto.builder()
      .orderAddress(dto.getOrderAddress())
      .memberId(memberId)
      .orderStatus(OrderStatus.INITIATED)
      .build();

    return orderService.createOrder(orderInfo)
      .flatMap(order ->
        // 카트에서 상품 정보 추출
        wishListService.getWishListByIds(dto.getWishListIds())
          .collectList()
          .flatMap(wishLists -> {
            // 주문 상세 넣는다 (OrderClothTable)
            List<OrderClothDto> orderClothDtos = wishLists.stream()
              .map(wishList -> OrderClothDto.builder()
                .quantity(wishList.getWishlistClothCount())
                .order(order)
                .clothDetailId(wishList.getClothDetailId())
                .build())
              .toList();
            return orderService.createOrderCloth(orderClothDtos);
          })
      )
      .then()
      .as(txOperator::transactional);
  }


  @DistributedLock(key = "#dto.clothDetailId")
  public Mono<Void> orderFromDetail(OrderRequestDto dto, Long memberId) {
    TransactionalOperator txOperator = TransactionalOperator.create(reactiveTransactionManager);
    return redisUtil.decreaseStock(String.valueOf(dto.getClothDetailId()), dto.getQuantity())
      .flatMap(result -> {
        if (!result) {
          return Mono.error(new CustomException(OrderExceptionEnum.CLOTH_INVENTORY_NOT_ENOUGH));
        }

        OrderDto orderInfo = OrderDto.builder()
          .orderAddress(dto.getOrderAddress())
          .memberId(memberId)
          .orderStatus(OrderStatus.INITIATED)
          .build();

        return orderService.createOrder(orderInfo)
          .flatMap(order -> {
            OrderClothDto orderClothDto = OrderClothDto.builder()
              .quantity(dto.getQuantity())
              .order(order)
              .clothDetailId(dto.getClothDetailId())
              .build();

            return orderService.createOrderCloth(new ArrayList<>(List.of(orderClothDto)))
              .then();
          });
      })
      .as(txOperator::transactional)
      .onErrorResume(e -> {
        e.printStackTrace();
        return redisUtil.increaseStock(String.valueOf(dto.getClothDetailId()), dto.getQuantity())
          .then(Mono.error(new CustomException(OrderExceptionEnum.ORDER_FAILED)));
      });
  }


  public Mono<Void> cancelOrder(Long orderId, Long memberId) {
    TransactionalOperator txOperator = TransactionalOperator.create(reactiveTransactionManager);

    return orderService.getOrderById(orderId)
      .flatMap(order -> {
        if (order.getOrderStatus() != OrderStatus.PAID) {
          return Mono.error(new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED));
        }
        // 주문 상태 변경
        return orderService.updateOrderStatus(orderId, OrderStatus.CANCEL);
      })
      .as(txOperator::transactional);
  }

  public Mono<Void> refundOrder(Long orderId, Long memberId) {
    TransactionalOperator txOperator = TransactionalOperator.create(reactiveTransactionManager);

    return orderService.getOrderById(orderId)
      .flatMap(order -> {
        if (order.getOrderStatus() != OrderStatus.COMPLETE) {
          return Mono.error(new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED));
        }
        if (LocalDateTime.now().isBefore(order.getDeliveryDate().plusDays(1))) {
          return Mono.error(new CustomException(OrderExceptionEnum.ORDER_STATUS_CANCEL_NOT_ALLOWED));
        }
        // 주문 상태 변경
        return orderService.updateOrderStatus(orderId, OrderStatus.REFUND);
      })
      .as(txOperator::transactional);
  }

}
