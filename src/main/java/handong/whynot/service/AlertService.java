package handong.whynot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import handong.whynot.domain.Account;
import handong.whynot.dto.alert.AlertResponseCode;
import handong.whynot.dto.alert.SMSCodeCheckRequestDTO;
import handong.whynot.dto.alert.SMSRequestDTO;
import handong.whynot.exception.alert.InvalidCodeException;
import handong.whynot.exception.alert.InvalidNumberLengthException;
import handong.whynot.exception.alert.MatchCodeFailException;
import handong.whynot.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

  private final String ACCOUNT_VERIFY_MESSAGE = "[WhyNotHere] 본인확인 인증번호(%s)를 입력해 주세요.";

  @Transactional
  public void sendSMS(String requestHost, Account account, SMSRequestDTO request) {
    log.info(requestHost + "에서 문자 전송을 시도하고 있습니다.");

//    if (! VERIFY_HOST.contains(requestHost)) {
//      log.error(requestHost + "에서 문자 전송을 시도하고 있습니다.");
//      throw new InvalidCodeException(AlertResponseCode.ALERT_INVALID_CODE);
//    }
    String targetNum = request.getPhoneNumber().replaceAll("-", "");
    if (targetNum.length() != 11) {
      throw new InvalidNumberLengthException(AlertResponseCode.ALERT_INVALID_NUMBER_LENGTH);
    }

    String code = RandomUtil.generateRandomNumber(6);
    String message = String.format(ACCOUNT_VERIFY_MESSAGE, code);
    account.generateSMSCode(code, request.getPhoneNumber());

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message sendMessage = Message.creator(
      new com.twilio.type.PhoneNumber("+82" + targetNum),
      new com.twilio.type.PhoneNumber(SENDER_NUM),
      message).create();

    account.setSmsSid(sendMessage.getSid());
  }

  @Transactional
  public void checkSMSCode(Account account, SMSCodeCheckRequestDTO request) {
    if (StringUtils.equals(account.getSmsCheckToken(), request.getSmsCode())) {
      account.setSmsVerified(true);
      return;
    }
    throw new MatchCodeFailException(AlertResponseCode.ALERT_MATCH_CODE_FAIL);
  }
}
