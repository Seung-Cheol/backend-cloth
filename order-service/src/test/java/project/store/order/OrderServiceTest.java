package project.store.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.store.order.client.ClothServiceClient;
import project.store.order.domain.entity.Order;
import project.store.order.domain.entity.OrderAddress;
import project.store.order.domain.entity.OrderCloth;
import project.store.order.domain.entity.OrderStatus;
import project.store.order.domain.repository.OrderClothRepository;
import project.store.order.domain.repository.OrderRepository;
import project.store.order.service.OrderService;
import project.store.order.service.dto.OrderDto;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceTest {

  private OrderService orderService;
  private OrderRepository orderRepository;
  private OrderClothRepository orderClothRepository;
  private ClothServiceClient clothServiceClient;

  private Order 주문;
  private OrderCloth 주문상품;

  @BeforeEach
  void setUp() {
    //mocking
    orderRepository = Mockito.mock(OrderRepository.class);
    orderClothRepository = Mockito.mock(OrderClothRepository.class);
    clothServiceClient = Mockito.mock(ClothServiceClient.class);
    orderService = new OrderService(
      orderRepository,orderClothRepository,clothServiceClient
    );
    주문 = Order.builder()
      .id(1L)
      .memberId(1L)
      .orderAddress(OrderAddress.builder()
        .address("서울시 강남구")
        .addressDetail("강남역 1번출구")
        .build())
      .orderStatus(OrderStatus.INITIATED)
      .build();

    주문상품 = OrderCloth.builder()
      .order(주문)
      .orderClothCount(1)
      .clothDetailId(1L)
      .build();

  }


  @DisplayName("주문 생성")
  @Test
  void createOrder() {
    //given
    when(orderRepository.save(any(Order.class))).thenReturn(주문);
    //when
    Order result = orderService.createOrder(
      OrderDto.builder()
      .memberId(1L)
      .orderAddress(OrderAddress.builder()
        .address("서울시 강남구")
        .addressDetail("강남역 1번출구")
        .build())
        .orderStatus(OrderStatus.INITIATED)
        .build()
      );
    //then
    assertNotNull(result);
    assertEquals(result.getOrderStatus(), OrderStatus.INITIATED);
  }

  @DisplayName("주문 완료")
  @Test
  void completeOrder() {
    //given
    when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(주문));
    //when
    orderService.updateOrderStatus(1L, OrderStatus.COMPLETE);
    //then
    assertEquals(주문.getOrderStatus(), OrderStatus.COMPLETE);
  }



}
