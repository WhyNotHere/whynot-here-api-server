package handong.whynot.dto.alert;

import handong.whynot.dto.common.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertResponseCode implements ResponseCode {
  ALERT_SEND_SMS_OK(20001, "SMS 발송에 성공하였습니다."),
  ALERT_CHECK_SMS_CODE_OK(20002, "SMS 인증 코드 확인에 성공하였습니다."),
  ALERT_SEND_SMS_FAIL(40001, "SMS 발송에 실패하였습니다."),
  ALERT_INVALID_CODE(40002, "유효하지 않은 코드입니다."),
  ALERT_MATCH_CODE_FAIL(40003, "제출한 인증 코드가 일치하지 않습니다."),
  ALERT_INVALID_NUMBER_LENGTH(40004, "번호 길이가 유효하지 않습니다.");

  private final Integer statusCode;
  private final String message;
}
