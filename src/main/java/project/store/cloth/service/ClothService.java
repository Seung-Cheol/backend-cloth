package project.store.cloth.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.store.cloth.api.dto.response.ClothDetailResponseDto;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothDetail;
import project.store.cloth.domain.repository.ClothDetailRepository;
import project.store.cloth.domain.repository.ClothRepository;

@Service
@RequiredArgsConstructor
public class ClothService {

  private final ClothRepository clothRepository;
  private final ClothDetailRepository clothDetailRepository;

  public List<ClothListResponseDto> getClothList(int page) {
    Pageable pageable = PageRequest.of(page - 1, 20);
    Page<Cloth> clothesPage = clothRepository.findAllLimit(pageable);
    List<Cloth> clothes = clothesPage.getContent();
    List<ClothListResponseDto> clothList =
      clothes.stream().map(ClothListResponseDto::toDto).collect(Collectors.toList());
    return clothList;
  }

  public ClothDetailResponseDto getClothDetail(Long id) {
    Cloth cloth = clothRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    ClothDetailResponseDto clothDetail = ClothDetailResponseDto.toDto(cloth);
    return clothDetail;
  }

  public void minusInventory(Long clothDetailId, int quantity) {
    ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    clothDetail.minusInventory(quantity);
    clothDetailRepository.save(clothDetail);
  }

  public void plusInventory(Long clothDetailId, int quantity) {
    ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    clothDetail.plusInventory(quantity);
    clothDetailRepository.save(clothDetail);
  }

  public ClothDetail getDetailEntity(Long clothDetailId) {
    ClothDetail clothDetail = clothDetailRepository.findById(clothDetailId)
      .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    return clothDetail;
  }
}
