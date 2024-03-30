package handong.whynot.dto.alert;

import handong.whynot.dto.common.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertResponseCode implements ResponseCode {
  ALERT_SEND_SMS_OK(20001, "SMS 발송에 성공하였습니다."),
  ALERT_SEND_SMS_FAIL(40001, "SMS 발송에 실패하였습니다."),
  ALERT_INVALID_CODE(40002, "유효하지 않은 코드입니다.");

  private final Integer statusCode;
  private final String message;
}
