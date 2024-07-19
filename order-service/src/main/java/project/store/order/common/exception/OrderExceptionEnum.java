package project.store.order.common.exception;


import lombok.Getter;

@Getter
public enum OrderExceptionEnum { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀
  ORDER_NOT_FOUND("420", "ORDER_001", "해당되는 id 의 결제 정보를 찾을 수 없습니다."),
  CLOTH_DETAIL_NOT_FOUND("421", "ORDER_002", "해당되는 id 의 옷 정보를 찾을 수 없습니다.");


  // 1. status = 날려줄 상태코드
  // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
  // 3. message = 발생한 예외에 대한 설명.

  private final String status;
  private final String code;
  private final String message;

  OrderExceptionEnum(String status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
  