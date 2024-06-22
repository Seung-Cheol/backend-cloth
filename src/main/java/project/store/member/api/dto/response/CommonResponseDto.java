package project.store.member.api.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class CommonResponseDto<T> {
  private String status;
  private String message;
  private T data;

  public CommonResponseDto(String status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public CommonResponseDto(String message) {
    this.message = message;
  }

  public static <T> CommonResponseDto<T> ofFail(String message) {
    return new CommonResponseDto<>("FAIL", message, null);
  }

  public static <T> CommonResponseDto<T> ofSuccess(String message) {
    return new CommonResponseDto<>("SUCEESS", message, null);
  }

  public static <T> CommonResponseDto<T> of(String message) {
    return new CommonResponseDto<>(message);
  }
}
