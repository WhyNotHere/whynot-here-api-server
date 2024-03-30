package handong.whynot.exception.alert;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class InvalidCodeException extends AbstractBaseException {
  public InvalidCodeException(ResponseCode responseCode) {
    super(responseCode);
  }
}
