package project.store.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.store.api.dto.response.CommonResponseDto;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(CustomException.class)
  public CommonResponseDto<?> handleGlobalException(CustomException e) {
    return CommonResponseDto.ofFail(e.getPaymentExceptionEnum().getStatus(), e.getPaymentExceptionEnum().getMessage());
  }


  @ExceptionHandler(Exception.class)
  public CommonResponseDto<?> handleGlobalException(Exception e) {
    return CommonResponseDto.ofFail("400","알 수 없는 오류 발생");
  }

}
