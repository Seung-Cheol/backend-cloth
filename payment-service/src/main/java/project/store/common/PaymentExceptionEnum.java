package project.store.common;


import lombok.Getter;

@Getter
public enum PaymentExceptionEnum { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀
  ORDER_NOT_FOUND("404", "PAYMENT_001", "해당되는 id 의 결제 정보를 찾을 수 없습니다."),
  ORDER_DETAIL_NOT_FOUND("404", "PAYMENT_002", "해당되는 id 의 주문 정보를 찾을 수 없습니다."),
  INVENTORY_NOT_SUFFICIENT("400", "PAYMENT_002", "재고가 부족합니다.");


  // 1. status = 날려줄 상태코드
  // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
  // 3. message = 발생한 예외에 대한 설명.

  private final String status;
  private final String code;
  private final String message;

  PaymentExceptionEnum(String status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
  