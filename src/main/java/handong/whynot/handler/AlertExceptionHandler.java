package handong.whynot.handler;

import handong.whynot.dto.alert.AlertResponseCode;
import handong.whynot.dto.common.ErrorResponseDTO;
import handong.whynot.exception.alert.InvalidCodeException;
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
}
