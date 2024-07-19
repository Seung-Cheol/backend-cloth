package project.store.member.common.exception;

public class CustomException extends RuntimeException {
  private final MemberExceptionEnum paymentExceptionEnum;

  public CustomException(MemberExceptionEnum paymentExceptionEnum) {
    this.paymentExceptionEnum = paymentExceptionEnum;
  }

  public MemberExceptionEnum getPaymentExceptionEnum() {
    return paymentExceptionEnum;
  }

}
