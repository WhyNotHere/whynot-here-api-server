package handong.whynot.interceptor;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.chat.ChatResponseCode;
import handong.whynot.exception.account.AccountTokenExpiredException;
import handong.whynot.exception.chat.PreSendFailException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Component
public class ChatInterceptor implements ChannelInterceptor {

  @Value("${jwt.secretKey}")
  private String jwtSecret;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    try {
      MessageHeaders headers = message.getHeaders();
      String messageType = headers.get("simpMessageType").toString();

      // MESSAGE(메세지 전송) 타입의 경우 accountId 세팅
      if (StringUtils.equals(messageType, SimpMessageType.MESSAGE.toString())) {
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) headers.get("nativeHeaders");
        List<String> authorizationList = nativeHeaders.get("Authorization");
        String authorization = authorizationList.get(0);

        SignedJWT signedJWT = SignedJWT.parse(authorization);
        signedJWT.verify(new MACVerifier(jwtSecret));
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        LocalDateTime tokenExpiredTime = LocalDateTime.ofInstant(jwtClaimsSet.getExpirationTime().toInstant(), ZoneOffset.UTC);
        if (tokenExpiredTime.isBefore(LocalDateTime.now())) {  // 지금보다 과거이면 true
          throw new AccountTokenExpiredException(AccountResponseCode.ACCOUNT_TOKEN_EXPIRED);
        }

        String accountId = jwtClaimsSet.getSubject();
        message = MessageBuilder.fromMessage(message)
          .setHeader("accountId", accountId)
          .build();
      }
    } catch (Exception e) {
      throw new PreSendFailException(ChatResponseCode.CHAT_PRESEND_FAIL);
    }

    return message;
  }
}