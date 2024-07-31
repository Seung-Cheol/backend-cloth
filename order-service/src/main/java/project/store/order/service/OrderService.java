package project.store.order.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.store.order.api.dto.response.ClothDetailResponseDto;
import project.store.order.api.dto.response.OrderListResponseDto;
import project.store.order.api.dto.response.WishListResponseDto;
import project.store.order.client.ClothServiceClient;
import project.store.order.common.CommonResponseDto;
import project.store.order.common.exception.CustomException;
import project.store.order.common.exception.OrderExceptionEnum;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderCloth;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.entity.WishList;
import project.store.order.domain.repository.OrderClothRepository;
import project.store.order.domain.repository.OrderRepository;
import project.store.order.service.dto.OrderClothDto;
import project.store.order.service.dto.OrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderClothRepository orderClothRepository;
  private final ClothServiceClient clothServiceClient;

  public Mono<Order> createOrder(OrderDto orderDto) {
    return Mono.fromCallable(() -> orderRepository.save(orderDto.createOrder(orderDto)).block());
  }

  public Mono<Void> createOrderCloth(List<OrderClothDto> orderClothDtos) {
    return Flux.fromIterable(orderClothDtos)
      .flatMap(orderClothDto -> Mono.fromCallable(() -> orderClothRepository.save(orderClothDto.createOrderCloth(orderClothDto))))
      .then();
  }

  public Flux<OrderListResponseDto> getOrderList(Long memberId) {
    return orderRepository.findAllByMemberId(memberId)
      .flatMap(order -> {
        List<Long> ids = order.getOrderCloths().stream().map(OrderCloth::getClothDetailId).toList();
        return clothServiceClient.getClothDetails(ids)
          .flatMapMany(clothDetails -> {
            List<ClothDetailResponseDto> clothDetailResponseDtos = order.getOrderCloths().stream()
              .map(orderCloth -> clothDetails.getData().stream()
                .filter(clothDetail -> clothDetail.getClothDetailId().equals(orderCloth.getClothDetailId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(OrderExceptionEnum.CLOTH_DETAIL_NOT_FOUND)))
              .toList();
            return Mono.just(OrderListResponseDto.builder()
              .orderId(order.getId())
              .orderStatus(order.getOrderStatus())
              .address(order.getOrderAddress().getAddress())
              .addressDetail(order.getOrderAddress().getAddressDetail())
              .orderDate(order.getOrderDate())
              .deliveryDate(order.getDeliveryDate())
              .totalPrice(clothDetailResponseDtos.stream().mapToInt(ClothDetailResponseDto::getPrice).sum())
              .clothDetailResponseDtos(clothDetailResponseDtos)
              .build());
          });
      })
      .filter(orderListResponseDto -> orderListResponseDto.getOrderStatus() != OrderStatus.INITIATED);
  }

  public Mono<Void> updateOrderStatus(Long orderId, OrderStatus status) {
    return orderRepository.findById(orderId)
      .switchIfEmpty(Mono.error(new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND)))
      .doOnNext(order -> order.updateStatus(status))
      .flatMap(orderRepository::save)
      .then();
  }

  public Flux<OrderCloth> getOrderClothByOrderId(Long orderId) {
    return orderRepository.findById(orderId)
      .switchIfEmpty(Mono.error(new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND)))
      .flatMapMany(orderClothRepository::findByOrder);
  }

  public Mono<Order> getOrderById(Long orderId) {
    return orderRepository.findById(orderId)
      .switchIfEmpty(Mono.error(new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND)));
  }
}
