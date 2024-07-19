package project.store.cloth.api;


import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.cloth.api.dto.response.ClothResponseDto;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.common.CommonResponseDto;
import project.store.cloth.service.ClothService;

@RestController
@RequiredArgsConstructor
@RequestMapping("cloth")
public class ClothController {
  private final ClothService clothService;

  @GetMapping("/list")
  public CommonResponseDto<List<ClothListResponseDto>> getClothList(int page) {
    List<ClothListResponseDto> clothList = clothService.getClothList(page);
    return CommonResponseDto.ofSuccess(clothList);
  }

  @GetMapping("/detail/{clothId}")
  public CommonResponseDto<ClothResponseDto> getClothDetail(@PathVariable Long clothId) {
    ClothResponseDto cloth = clothService.getClothDetail(clothId);
    return CommonResponseDto.ofSuccess(cloth);
  }

  @PostMapping("/inventory")
  public CommonResponseDto<Map<Long, Integer>> getInventory(@RequestBody List<Long> clothDetailIds) {
    Map<Long, Integer> inventoryMap = clothService.getInventory(clothDetailIds);
    return CommonResponseDto.ofSuccess(inventoryMap);
  }

  @PostMapping("/details")
  public CommonResponseDto<?> getClothDetails(@RequestBody List<Long> clothDetailIds) {
    return CommonResponseDto.ofSuccess(clothService.getClothDetails(clothDetailIds));
  }
}
