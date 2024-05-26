package handong.whynot.dto.blind_date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealDataResponseDTO {
    private GenderData gender;
    private FaithData faith;
    private LocationData location;
}
