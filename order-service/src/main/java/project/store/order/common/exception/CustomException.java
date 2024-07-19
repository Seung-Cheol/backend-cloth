package project.store.order.common.exception;

public class CustomException extends RuntimeException {
  private final OrderExceptionEnum paymentExceptionEnum;

  public CustomException(OrderExceptionEnum paymentExceptionEnum) {
    this.paymentExceptionEnum = paymentExceptionEnum;
  }

  public OrderExceptionEnum getPaymentExceptionEnum() {
    return paymentExceptionEnum;
  }

}
