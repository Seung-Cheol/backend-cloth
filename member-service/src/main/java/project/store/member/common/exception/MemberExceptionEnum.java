package project.store.member.common.exception;


import lombok.Getter;

@Getter
public enum MemberExceptionEnum { // 예외 발생시, body에 실어 날려줄 상태, code, message 커스텀
  MEMBER_ALREADY_EXISTS("410", "MEMBER_001", "이미 존재하는 회원입니다."),
  EMAIL_SEND_FAIL("411", "MEMBER_002", "이메일 전송에 실패했습니다."),
  EMAIL_CODE_NOT_MATCH("412", "MEMBER_003", "인증코드가 일치하지 않습니다."),
  REFRESH_TOKEN_EXPIRED("413", "MEMBER_004", "리프레시 토큰이 만료되었거나 존재하지 않습니다."),
  MEMBER_NOT_FOUND("414", "MEMBER_005", "존재하지 않는 회원입니다."),
  LOGIN_FAILED("415", "MEMBER_006", "로그인에 실패했습니다.");


  // 1. status = 날려줄 상태코드
  // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
  // 3. message = 발생한 예외에 대한 설명.

  private final String status;
  private final String code;
  private final String message;

  MemberExceptionEnum(String status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
  