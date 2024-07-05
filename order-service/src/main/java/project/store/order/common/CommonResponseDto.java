package project.store.order.common;

import lombok.Data;

@Data
public class CommonResponseDto<T> {
  private String status = "200";
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


  public CommonResponseDto(String message, T data) {
    this.message = message;
    this.data = data;
  }

  public static <T> CommonResponseDto<T> ofFail(String message) {
    return new CommonResponseDto<>("FAIL", message, null);
  }

  public static <T> CommonResponseDto<T> ofData(String message,T data) {
    return new CommonResponseDto<>(message, data);
  }

  public static <T> CommonResponseDto<T> of(String message) {
    return new CommonResponseDto<>(message);
  }
}
