package project.store.common;

public class CustomException extends RuntimeException {
  private final PaymentExceptionEnum paymentExceptionEnum;

  public CustomException(PaymentExceptionEnum paymentExceptionEnum) {
    this.paymentExceptionEnum = paymentExceptionEnum;
  }

  public PaymentExceptionEnum getPaymentExceptionEnum() {
    return paymentExceptionEnum;
  }

}
