package project.store.cloth.api;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.store.cloth.api.dto.response.ClothDetailResponseDto;
import project.store.cloth.api.dto.response.ClothListResponseDto;
import project.store.cloth.service.ClothService;
import project.store.member.api.dto.response.CommonResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("cloth")
public class ClothController {
  private final ClothService clothService;

  @GetMapping("/list")
  public CommonResponseDto<List<ClothListResponseDto>> getClothList(int page) {
    List<ClothListResponseDto> clothList = clothService.getClothList(page);
    return CommonResponseDto.ofData("성공", clothList);
  }

  @GetMapping("/detail/{clothId}")
  public CommonResponseDto<ClothDetailResponseDto> getClothDetail(@PathVariable Long clothId) {
    ClothDetailResponseDto cloth = clothService.getClothDetail(clothId);
    return CommonResponseDto.ofData("성공", cloth);
  }
}
