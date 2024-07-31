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

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderClothRepository orderClothRepository;
  private final ClothServiceClient clothServiceClient;

  public Order createOrder(OrderDto orderDto) {
    return orderRepository.save(orderDto.createOrder(orderDto));
  }

  public void createOrderCloth(List<OrderClothDto> orderClothDtos) {
    for(OrderClothDto orderClothDto : orderClothDtos) {
      orderClothRepository.save(orderClothDto.createOrderCloth(orderClothDto));
    }
  }


  @Transactional
  public List<OrderListResponseDto> getOrderList(Long memberId) {
    List<Order> list = orderRepository.findAllByMemberId(memberId);
    List<Long> ids = list.stream().map(Order::getOrderCloths).flatMap(orderCloths -> orderCloths.stream().map(OrderCloth::getClothDetailId)).toList();

    CommonResponseDto<List<ClothDetailResponseDto>> clothDetails = clothServiceClient.getClothDetails(ids);

    List<OrderListResponseDto> data = list.stream().map(order -> {
      List<OrderCloth> orderCloths = order.getOrderCloths();
      List<ClothDetailResponseDto> clothDetailResponseDtos =
        orderCloths.stream().map(orderCloth -> clothDetails.getData().stream()
        .filter(clothDetail -> clothDetail.getClothDetailId().equals(orderCloth.getClothDetailId()))
        .findFirst()
        .orElseThrow(() -> new CustomException(OrderExceptionEnum.CLOTH_DETAIL_NOT_FOUND))).toList();
      System.out.println(order.getOrderAddress());
      return OrderListResponseDto.builder()
        .orderId(order.getId())
        .orderStatus(order.getOrderStatus())
        .address(order.getOrderAddress().getAddress())
        .addressDetail(order.getOrderAddress().getAddressDetail())
        .orderDate(order.getOrderDate())
        .deliveryDate(order.getDeliveryDate())
        .totalPrice(clothDetailResponseDtos.stream().mapToInt(ClothDetailResponseDto::getPrice).sum())
        .clothDetailResponseDtos(clothDetailResponseDtos)
        .build();
    }).filter(
      orderListResponseDto -> orderListResponseDto.getOrderStatus() != OrderStatus.INITIATED
    ).toList();

    return data;
  }

  public void updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND));
    order.updateStatus(status);
    orderRepository.save(order);
  }

  public List<OrderCloth> getOrderClothByOrderId(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND));
    return orderClothRepository.findByOrder(order);
  }

  public Order getOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(() -> new CustomException(OrderExceptionEnum.ORDER_NOT_FOUND));
  }


}
