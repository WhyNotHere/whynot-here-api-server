package handong.whynot.exception.chat;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class InvalidParticipantException extends AbstractBaseException {
  public InvalidParticipantException(ResponseCode responseCode) {
    super(responseCode);
  }
}
