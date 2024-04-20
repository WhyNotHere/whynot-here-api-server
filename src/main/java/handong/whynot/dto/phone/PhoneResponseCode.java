package handong.whynot.dto.phone;

import handong.whynot.dto.common.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhoneResponseCode implements ResponseCode {

    PHONE_CREATE_OK(20001, "연락처 동기화에 성공하였습니다.");

    private final Integer statusCode;
    private final String message;
}
