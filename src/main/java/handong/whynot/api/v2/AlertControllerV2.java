package handong.whynot.api.v2;

import handong.whynot.domain.Account;
import handong.whynot.dto.alert.AlertResponseCode;
import handong.whynot.dto.alert.SMSCodeCheckRequestDTO;
import handong.whynot.dto.alert.SMSRequestDTO;
import handong.whynot.dto.common.ResponseDTO;
import handong.whynot.service.AccountService;
import handong.whynot.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v2/alert")
@RequiredArgsConstructor
public class AlertControllerV2 {

    private final AlertService alertService;
    private final AccountService accountService;

    @Operation(summary = "사용자 인증 문자 발송")
    @PostMapping("/sms")
    public ResponseDTO sendSMS(HttpServletRequest httpRequest, @RequestBody SMSRequestDTO request) {
        Account account = accountService.getCurrentAccount();
        alertService.sendSMS(httpRequest.getRemoteHost(), account, request);
        return ResponseDTO.of(AlertResponseCode.ALERT_SEND_SMS_OK);
    }

    @Operation(summary = "인증 코드 확인")
    @PostMapping("/check-sms-code")
    public ResponseDTO checkSMSCode(@RequestBody SMSCodeCheckRequestDTO request) {
        Account account = accountService.getCurrentAccount();
        alertService.checkSMSCode(account, request);
        return ResponseDTO.of(AlertResponseCode.ALERT_CHECK_SMS_CODE_OK);
    }
}
