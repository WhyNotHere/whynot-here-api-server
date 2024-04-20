package handong.whynot.handler;

import handong.whynot.dto.blind_date.BlindDateResponseCode;
import handong.whynot.dto.chat.ChatResponseCode;
import handong.whynot.dto.common.ErrorResponseDTO;
import handong.whynot.exception.blind_date.BlindDateNotFoundException;
import handong.whynot.exception.chat.ChatRoomNotFoundException;
import handong.whynot.exception.chat.InvalidParticipantException;
import handong.whynot.exception.chat.InvalidTokenException;
import handong.whynot.exception.chat.PreSendFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ChatExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidTokenException.class)
    ErrorResponseDTO invalidTokenExceptionHandle() {
        return ErrorResponseDTO.of(ChatResponseCode.CHAT_INVALID_TOKEN, null);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidParticipantException.class)
    ErrorResponseDTO invalidParticipantExceptionHandle() {
        return ErrorResponseDTO.of(ChatResponseCode.CHAT_INVALID_PARTICIPANT, null);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(PreSendFailException.class)
    ErrorResponseDTO preSendFailExceptionHandle() {
        return ErrorResponseDTO.of(ChatResponseCode.CHAT_PRESEND_FAIL, null);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ErrorResponseDTO blindDateNotFoundExceptionHandle() {
        return ErrorResponseDTO.of(ChatResponseCode.CHAT_ROOM_READ_FAIL, null);
    }
}
