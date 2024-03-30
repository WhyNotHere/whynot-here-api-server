package handong.whynot.service;

import handong.whynot.dto.alert.AlertResponseCode;
import handong.whynot.dto.alert.SMSRequestDTO;
import handong.whynot.exception.alert.InvalidCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

  @Value("${sms.token.accountId}")
  public String ACCOUNT_SID;

  @Value("${sms.token.authId}")
  public String AUTH_TOKEN;

  @Value("${sms.token.senderNum}")
  public String SENDER_NUM;

  @Value("${sms.token.verifyHost}")
  public String VERIFY_HOST;

  public void sendSMS(String requestHost, SMSRequestDTO request) {
    if (! VERIFY_HOST.contains(requestHost)) {
      log.error(requestHost + "에서 문자 전송을 시도하고 있습니다.");
      throw new InvalidCodeException(AlertResponseCode.ALERT_INVALID_CODE);
    }

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
      new com.twilio.type.PhoneNumber("+82" + request.getPhoneNumber().replaceAll("-", "")),
      new com.twilio.type.PhoneNumber(SENDER_NUM),
      request.getMessage()).create();

    System.out.println(message.getSid());
  }
}
