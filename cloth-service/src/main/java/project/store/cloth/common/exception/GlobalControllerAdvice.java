package project.store.cloth.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.store.cloth.common.CommonResponseDto;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(CustomException.class)
  public CommonResponseDto<?> handleGlobalException(CustomException e) {
    return CommonResponseDto.ofFail(e.getPaymentExceptionEnum().getStatus(), e.getPaymentExceptionEnum().getMessage());
  }

  @ExceptionHandler(Exception.class)
  public CommonResponseDto<?> handleGlobalException(Exception e) {
    e.printStackTrace();
    return CommonResponseDto.ofFail("400","알 수 없는 오류 발생");
  }

}
