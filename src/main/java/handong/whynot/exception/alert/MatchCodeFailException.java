package handong.whynot.exception.alert;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class MatchCodeFailException extends AbstractBaseException {
  public MatchCodeFailException(ResponseCode responseCode) {
    super(responseCode);
  }
}
