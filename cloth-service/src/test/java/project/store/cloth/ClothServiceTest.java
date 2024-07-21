package project.store.cloth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.api.dto.response.ClothResponseDto;
import project.store.cloth.common.util.RedisUtil;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothSize;
import project.store.cloth.domain.ClothType;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.cloth.domain.repository.ClothOutboxRepository;
import project.store.cloth.domain.repository.ClothRepository;
import project.store.cloth.kafka.ClothProducer;
import project.store.cloth.service.ClothService;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClothServiceTest {

    private ClothRepository clothRepository;
    private ClothDetailRepository clothDetailRepository;
    private ClothOutboxRepository clothOutboxRepository;
    private ClothProducer clothProducer;
    private ClothService clothService;



    private Cloth 옷정보;
    private ClothDetail 옷상세정보;


    @BeforeEach
    void setUp() {
      //mocking
      clothRepository = Mockito.mock(ClothRepository.class);
      clothDetailRepository = Mockito.mock(ClothDetailRepository.class);
      clothOutboxRepository = Mockito.mock(ClothOutboxRepository.class);
      clothProducer = Mockito.mock(ClothProducer.class);
      clothService = new ClothService(
        clothRepository,
        clothDetailRepository,
        clothOutboxRepository,
        clothProducer
      );

      옷상세정보 = ClothDetail.builder()
        .id(1L)
        .inventory(100)
        .size(ClothSize.S)
        .cloth(옷정보)
        .build();

      옷정보 = Cloth.builder()
        .id(1L)
        .clothName("[30대 구매 1위] 데님 팬츠")
        .clothContent("30대 여성들이 가장 많이 구매한 데님 팬츠입니다.")
        .thumbnail("옷썸네일")
        .price(10000)
        .limited(false)
        .startAt(LocalDateTime.now())
        .created_at(LocalDate.now())
        .clothType(new ClothType(1L, "바지"))
        .clothDetails(Arrays.asList(옷상세정보))
        .build();
    }

    @DisplayName("옷 목록 조회")
    @Test
    void getClothList() {
      //given
      Page<Cloth> mockedPage = new PageImpl<>(Arrays.asList(옷정보));
      when(clothRepository.findAllLimit(PageRequest.of(0, 20))).thenReturn(mockedPage);

      //when
      List<ClothListResponseDto> list = clothService.getClothList(1);

      //then
      assertEquals(list.get(0).getClothName(), 옷정보.getClothName());
    }

    @DisplayName("옷 상세 조회")
    @Test
    void getClothDetail() {
      //given
      when(clothRepository.findById(1L)).thenReturn(java.util.Optional.of(옷정보));

      //when
      ClothResponseDto clothDetail = clothService.getClothDetail(1L);

      //then
      assertEquals(clothDetail.getClothDetails().get(0).getInventory(), 옷상세정보.getInventory());
    }


}
