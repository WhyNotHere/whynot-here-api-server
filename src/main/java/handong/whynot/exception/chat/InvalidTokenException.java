package handong.whynot.exception.chat;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class InvalidTokenException extends AbstractBaseException {
  public InvalidTokenException(ResponseCode responseCode) {
    super(responseCode);
  }
}
