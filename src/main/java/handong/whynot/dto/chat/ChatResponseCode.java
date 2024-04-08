package handong.whynot.dto.chat;

import handong.whynot.dto.common.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatResponseCode implements ResponseCode {
  CHAT_OK(20001, "성공하였습니다."),
  CHAT_INVALID_TOKEN(40001, "메세지 전송자의 자격 증명이 유효하지 않습니다."),
  CHAT_INVALID_PARTICIPANT(40002, "채팅방에 참여한 사용자가 아닙니다."),
  CHAT_PRESEND_FAIL(40003, "메세지 전처리에 실패했습니다."),
  CHAT_ROOM_READ_FAIL(40004, "채팅방 조회에 실패했습니다");

  private final Integer statusCode;
  private final String message;
}
