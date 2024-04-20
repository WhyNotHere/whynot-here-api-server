package handong.whynot.service;

import handong.whynot.domain.Account;
import handong.whynot.domain.Phone;
import handong.whynot.dto.phone.PhoneDTO;
import handong.whynot.dto.phone.PhonesRequestDTO;
import handong.whynot.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;

    @Transactional
    public void syncPhones(Account account, PhonesRequestDTO request) {

        // 기존 연락처 제거
        phoneRepository.deleteAllByAccount(account);

        List<Phone> phones = new ArrayList<>();
        for (PhoneDTO phone : request.getPhones()) {
            phones.add(Phone.builder()
                    .name(phone.getName())
                    .phone(phone.getPhone())
                    .useYn(phone.getUseYn())
                    .account(account)
                    .build());
        }
        phoneRepository.saveAll(phones);
    }

    public List<PhoneDTO> getMyPhones(Account account) {
        return phoneRepository.findByAccount(account).stream()
                .map(PhoneDTO::of)
                .collect(Collectors.toList());
    }
}
