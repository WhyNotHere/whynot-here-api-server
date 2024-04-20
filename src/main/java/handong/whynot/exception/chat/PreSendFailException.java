package handong.whynot.exception.chat;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class PreSendFailException extends AbstractBaseException {
  public PreSendFailException(ResponseCode responseCode) {
    super(responseCode);
  }
}
