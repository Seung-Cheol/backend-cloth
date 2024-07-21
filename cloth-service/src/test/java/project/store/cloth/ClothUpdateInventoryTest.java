package project.store.cloth;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothSize;
import project.store.cloth.domain.ClothType;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.cloth.domain.repository.ClothRepository;
import project.store.cloth.domain.repository.ClothTypeRepository;
import project.store.cloth.service.ClothService;

@SpringBootTest
@ActiveProfiles("test")
public class ClothUpdateInventoryTest {

  @Autowired
  private ClothService clothService;
  @Autowired
  private ClothRepository clothRepository;
  @Autowired
  private ClothDetailRepository clothDetailRepository;
  @Autowired
  private ClothTypeRepository clothTypeRepository;

  @BeforeEach
  void setUp() {
    clothRepository.deleteAllInBatch();
    clothDetailRepository.deleteAllInBatch();
    clothTypeRepository.deleteAllInBatch();

    ClothType 바지 = ClothType.builder().typeName("바지").build();

    Cloth 옷정보 = Cloth.builder()
      .id(1L)
      .clothName("[30대 구매 1위] 데님 팬츠")
      .clothContent("30대 여성들이 가장 많이 구매한 데님 팬츠입니다.")
      .thumbnail("옷썸네일")
      .price(10000)
      .limited(false)
      .startAt(LocalDateTime.now())
      .created_at(LocalDate.now())
      .clothType(new ClothType(1L, "바지"))
      .build();

    ClothDetail 옷상세정보 = ClothDetail.builder()
      .id(1L)
      .inventory(100)
      .size(ClothSize.S)
      .cloth(옷정보)
      .build();
    clothTypeRepository.save(바지);
    clothRepository.save(옷정보);
    clothDetailRepository.save(옷상세정보);
  }

  @DisplayName("재고 감소 동시성 테스트")
  @Test
  void updateInventory() throws InterruptedException {
    //given
    ExecutorService executorService = Executors.newFixedThreadPool(50);
    CountDownLatch latch = new CountDownLatch(50);
    //when
    for (int i = 0; i < 50; i++) {
      executorService.execute(() -> {
        clothService.updateInventory(1L, 1L, 1);
        latch.countDown();
      });
    }
    latch.await();
    executorService.shutdown();

    //then
    assertEquals(clothDetailRepository.findById(1L).get().getInventory(), 50);
  }

}
