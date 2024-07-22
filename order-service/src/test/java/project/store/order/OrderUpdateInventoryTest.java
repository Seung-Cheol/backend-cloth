package project.store.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import jakarta.transaction.Transactional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.store.order.api.dto.request.OrderRequestDto;
import project.store.order.common.util.RedisUtil;
import project.store.order.service.OrderService;
import project.store.order.service.facade.OrderFacade;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderUpdateInventoryTest {

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderFacade orderFacade;

  @BeforeEach
  void setUp() {
    redisUtil.setData("1", "100");
  }

  @DisplayName("재고 차감")
  @Test
  void updateInventory() {
    // given
    Long clothDetailId = 1L;
    int quantity = 1;

    // when
    orderFacade.orderFromDetail(OrderRequestDto.builder()
      .clothDetailId(clothDetailId)
      .quantity(quantity)
      .build(), 1L);

    // then
    assertEquals(99, Integer.parseInt(redisUtil.getData("1")));
  }

  @DisplayName("동시 재고 차감")
  @Test
  void updateInventoryConcurrent() throws InterruptedException {
    // given
    Long clothDetailId = 1L;
    int quantity = 1;
    Long memberId = 1L;
    ExecutorService executorService = Executors.newFixedThreadPool(99);
    CountDownLatch latch = new CountDownLatch(99);
    //when
    for (int i = 0; i < 99; i++) {
      executorService.execute(() -> {
        orderFacade.orderFromDetail(OrderRequestDto.builder()
          .clothDetailId(clothDetailId)
          .quantity(quantity)
          .build(), memberId);
        latch.countDown();
      });
    }
    latch.await();
    executorService.shutdown();

    // then
    assertEquals(1, Integer.parseInt(redisUtil.getData("1")));
  }

}
