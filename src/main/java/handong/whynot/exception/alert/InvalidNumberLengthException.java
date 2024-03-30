package handong.whynot.exception.alert;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class InvalidNumberLengthException extends AbstractBaseException {
  public InvalidNumberLengthException(ResponseCode responseCode) {
    super(responseCode);
  }
}
