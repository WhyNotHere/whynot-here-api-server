package handong.whynot.dto.phone;

import handong.whynot.domain.Phone;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhoneDTO {
    private String name;
    private String phone;
    private String useYn;

    public static PhoneDTO of(Phone phone) {
        return builder()
                .name(phone.getName())
                .phone(phone.getPhone())
                .useYn(phone.getUseYn())
                .build();
    }
}
