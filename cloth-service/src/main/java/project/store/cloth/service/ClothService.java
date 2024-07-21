package project.store.cloth.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.store.cloth.api.dto.response.ClothDetailResponseDto;
import project.store.cloth.api.dto.response.ClothResponseDto;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.common.exception.ClothExceptionEnum;
import project.store.cloth.common.exception.CustomException;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.ClothOutbox;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.cloth.domain.repository.ClothOutboxRepository;
import project.store.cloth.domain.repository.ClothRepository;
import project.store.cloth.kafka.ClothProducer;
import project.store.cloth.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class ClothService {

  private final ClothRepository clothRepository;
  private final ClothDetailRepository clothDetailRepository;
  private final ClothOutboxRepository clothOutboxRepository;
  private final ClothProducer clothProducer;

  private final static String ORDER_ROLLBACK = "order_rollback";
  private final static String PAYMENT_ROLLBACK = "payment_rollback";

  public List<ClothListResponseDto> getClothList(int page) {
    Pageable pageable = PageRequest.of(page - 1, 20);
    Page<Cloth> clothesPage = clothRepository.findAllLimit(pageable);
    List<Cloth> clothes = clothesPage.getContent();
    List<ClothListResponseDto> clothList =
      clothes.stream().map(ClothListResponseDto::toDto).collect(Collectors.toList());
    return clothList;
  }

  public ClothResponseDto getClothDetail(Long id) {
    Cloth cloth = clothRepository.findById(id).orElseThrow(() -> new CustomException(
      ClothExceptionEnum.CLOTH_NOT_FOUND));
    ClothResponseDto clothDetail = ClothResponseDto.toDto(cloth);
    return clothDetail;
  }



  public ClothDetail getDetailEntity(Long clothDetailId) {
    ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId)
      .orElseThrow(() -> new CustomException(ClothExceptionEnum.CLOTH_NOT_FOUND));
    return clothDetail;
  }

  public Map<Long, Integer> getInventory(List<Long> clothDetailIds) {
    Map<Long, Integer> inventoryMap = new HashMap<>();
    for (Long id : clothDetailIds) {
      ClothDetail clothDetail = clothDetailRepository.findById(id)
        .orElseThrow(() -> new CustomException(ClothExceptionEnum.CLOTH_NOT_FOUND));
      inventoryMap.put(id, clothDetail.getInventory());
    }
    return inventoryMap;
  }

  public List<ClothDetailResponseDto> getClothDetails(List<Long> clothDetailIds) {
    List<ClothDetail> clothDetails = clothDetailRepository.findAllById(clothDetailIds);
    List<ClothDetailResponseDto> clothDetailResponseDtos = clothDetails.stream()
      .map(ClothDetailResponseDto::toDto)
      .collect(Collectors.toList());
    return clothDetailResponseDtos;
  }

  @DistributedLock(key = "#clothDetailId")
  public void updateInventory(Long orderId, Long clothDetailId, int quantity) {
    try {
      System.out.println("start");
      ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId)
        .orElseThrow(() -> new CustomException(ClothExceptionEnum.CLOTH_NOT_FOUND));
      if(clothDetail.getInventory() < quantity) {
        throw new CustomException(ClothExceptionEnum.INVENTORY_NOT_ENOUGH);
      }
      clothDetail.updateInventory(clothDetail.getInventory() - quantity);
      clothDetailRepository.save(clothDetail);
    } catch (Exception e) {
      clothOutboxRepository.save(ClothOutbox.builder()
        .topic(ORDER_ROLLBACK)
        .message(orderId.toString())
        .isSent(false)
        .build());
      clothOutboxRepository.save(ClothOutbox.builder()
        .topic(PAYMENT_ROLLBACK)
        .message(orderId.toString())
        .isSent(false)
        .build());
      clothProducer.send();
    }
  }
}
