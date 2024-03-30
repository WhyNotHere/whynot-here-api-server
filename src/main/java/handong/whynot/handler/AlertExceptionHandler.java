package handong.whynot.handler;

import handong.whynot.dto.alert.AlertResponseCode;
import handong.whynot.dto.common.ErrorResponseDTO;
import handong.whynot.exception.alert.InvalidCodeException;
import handong.whynot.exception.alert.InvalidNumberLengthException;
import handong.whynot.exception.alert.MatchCodeFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class AlertExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidCodeException.class)
    ErrorResponseDTO invalidCodeExceptionHandle() {
        return ErrorResponseDTO.of(AlertResponseCode.ALERT_INVALID_CODE, null);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidNumberLengthException.class)
    ErrorResponseDTO invalidNumberLengthExceptionHandle() {
        return ErrorResponseDTO.of(AlertResponseCode.ALERT_INVALID_NUMBER_LENGTH, null);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MatchCodeFailException.class)
    ErrorResponseDTO matchCodeFailExceptionHandle() {
        return ErrorResponseDTO.of(AlertResponseCode.ALERT_MATCH_CODE_FAIL, null);
    }
}
