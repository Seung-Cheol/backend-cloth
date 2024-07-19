package project.store.member.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonResponseDto<T> {
  private String status = "200";
  private String message = "SUCCESS";
  private T data;

  public CommonResponseDto(String status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }


  public CommonResponseDto(T data) {
    this.data = data;
  }


  public CommonResponseDto(String status, String message) {
    this.status = status;
    this.message = message;
  }

  public static <T> CommonResponseDto<T> ofFail(String status, String message) {
    return new CommonResponseDto<>(status, message);
  }

  public static <T> CommonResponseDto<T> ofSuccess(T data) {
    return new CommonResponseDto<>(data);
  }
}

