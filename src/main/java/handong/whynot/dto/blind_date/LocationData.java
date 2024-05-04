package handong.whynot.dto.blind_date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LocationData {
    private List<String> topList;
    private List<String> fullList;
}
