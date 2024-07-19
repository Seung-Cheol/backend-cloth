package project.store.cloth.common.exception;

public class CustomException extends RuntimeException {
  private final ClothExceptionEnum paymentExceptionEnum;

  public CustomException(ClothExceptionEnum paymentExceptionEnum) {
    this.paymentExceptionEnum = paymentExceptionEnum;
  }

  public ClothExceptionEnum getPaymentExceptionEnum() {
    return paymentExceptionEnum;
  }

}
