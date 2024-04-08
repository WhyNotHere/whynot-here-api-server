package handong.whynot.exception.chat;

import handong.whynot.dto.common.ResponseCode;
import handong.whynot.exception.AbstractBaseException;

public class ChatRoomNotFoundException extends AbstractBaseException {
  public ChatRoomNotFoundException(ResponseCode responseCode) {
    super(responseCode);
  }
}
