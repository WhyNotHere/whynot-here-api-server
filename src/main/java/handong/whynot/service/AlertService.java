package handong.whynot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import handong.whynot.domain.Account;
import handong.whynot.dto.alert.SMSRequestDTO;
import handong.whynot.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

  private String ACCOUNT_VERIFY_MESSAGE = "[WhyNotHere] 본인확인 인증번호(%s)를 입력해 주세요.";

  @Transactional
  public void sendSMS(String requestHost, Account account, SMSRequestDTO request) {
    log.info(requestHost + "에서 문자 전송을 시도하고 있습니다.");

//    if (! VERIFY_HOST.contains(requestHost)) {
//      log.error(requestHost + "에서 문자 전송을 시도하고 있습니다.");
//      throw new InvalidCodeException(AlertResponseCode.ALERT_INVALID_CODE);
//    }

    String code = RandomUtil.generateRandomNumber(6);
    ACCOUNT_VERIFY_MESSAGE = String.format(ACCOUNT_VERIFY_MESSAGE, code);
    account.generateSMSCode(code, request.getPhoneNumber());

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
      new com.twilio.type.PhoneNumber("+82" + request.getPhoneNumber().replaceAll("-", "")),
      new com.twilio.type.PhoneNumber(SENDER_NUM),
      ACCOUNT_VERIFY_MESSAGE).create();

    account.setSmsSid(message.getSid());
  }
}
