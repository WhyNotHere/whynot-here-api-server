package handong.whynot.exception.account;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class OAuth2NotProvidedException extends AbstractBaseException {

    public OAuth2NotProvidedException(ResponseCode responseCode) {
        super(responseCode);
    }
}
