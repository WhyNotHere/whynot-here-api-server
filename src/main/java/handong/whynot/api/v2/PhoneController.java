package handong.whynot.api.v2;

import handong.whynot.domain.Account;
import handong.whynot.dto.common.ResponseDTO;
import handong.whynot.dto.phone.PhoneDTO;
import handong.whynot.dto.phone.PhoneResponseCode;
import handong.whynot.dto.phone.PhonesRequestDTO;
import handong.whynot.service.AccountService;
import handong.whynot.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;
    private final AccountService accountService;

    @Operation(summary = "연락처 동기화")
    @PostMapping("/sync-phones")
    public ResponseDTO syncPhones(@RequestBody PhonesRequestDTO request) {
        Account account = accountService.getCurrentAccount();
        phoneService.syncPhones(account, request);
        return ResponseDTO.of(PhoneResponseCode.PHONE_CREATE_OK);
    }

    @Operation(summary = "본인 연락처 조회")
    @GetMapping("")
    public List<PhoneDTO> getMyPhones() {
        Account account = accountService.getCurrentAccount();
        return phoneService.getMyPhones(account);
    }
}
