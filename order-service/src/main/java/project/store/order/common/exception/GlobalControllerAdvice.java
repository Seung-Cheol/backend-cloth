package project.store.order.common.exception;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.store.order.common.CommonResponseDto;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(CustomException.class)
  public CommonResponseDto<?> handleGlobalException(CustomException e) {
    return CommonResponseDto.ofFail(e.getPaymentExceptionEnum().getStatus(), e.getPaymentExceptionEnum().getMessage());
  }

  @ExceptionHandler(CallNotPermittedException.class)
  public CommonResponseDto<?> handleGlobalException(CallNotPermittedException e) {
    List<?> arr = new ArrayList<>();
    return CommonResponseDto.ofSuccess(arr);
  }

  @ExceptionHandler(Exception.class)
  public CommonResponseDto<?> handleGlobalException(Exception e) {
    e.printStackTrace();
    return CommonResponseDto.ofFail("400","알 수 없는 오류 발생");
  }

}
