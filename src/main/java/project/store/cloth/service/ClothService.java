package project.store.cloth.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.quota.ClientQuotaAlteration.Op;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.store.cloth.api.dto.response.ClothDetailResponseDto;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.domain.Cloth;
import project.store.cloth.domain.ClothPicture;
import project.store.cloth.domain.repository.ClothRepository;

@Service
@RequiredArgsConstructor
public class ClothService {

  private final ClothRepository clothRepository;

  public List<ClothListResponseDto> getClothList(int page) {
    Pageable pageable = PageRequest.of(page - 1, 20);
    Page<Cloth> clothesPage = clothRepository.findAllLimit(pageable);
    List<Cloth> clothes = clothesPage.getContent();
    List<ClothListResponseDto> clothList = clothes.stream().map(cloth ->
      ClothListResponseDto.builder()
        .id(cloth.getId())
        .clothName(cloth.getClothName())
        .thumbnail(cloth.getThumbnail())
        .price(cloth.getPrice())
        .clothType(cloth.getClothType())
        .build()).collect(Collectors.toList());
    return clothList;
  }

  public ClothDetailResponseDto getClothDetail(Long id) {
    Cloth cloth = clothRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    ClothDetailResponseDto clothDetail = ClothDetailResponseDto.builder()
      .id(cloth.getId())
      .clothName(cloth.getClothName())
      .clothContent(cloth.getClothContent())
      .thumbnail(cloth.getThumbnail())
      .price(cloth.getPrice())
      .created_at(cloth.getCreated_at())
      .clothType(cloth.getClothType())
      .clothDetails(cloth.getClothDetails())
      .clothPictures(cloth.getClothPictures())
      .build();
    return clothDetail;
  }
}
