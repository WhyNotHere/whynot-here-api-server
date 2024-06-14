package handong.whynot.dto.blind_date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import handong.whynot.domain.BlindDate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class HistoryResponseDTO {

    private static final String DATE_PATTERN = "yyyy.MM.dd";
    private static final LocalDateTime[] seasonDate = {
            LocalDateTime.of(2023, 1, 1, 0, 0),  // dummy
            LocalDateTime.of(2023, 9, 11, 0, 0),  // 재학생 시즌 1
            LocalDateTime.of(2024, 1, 6, 12, 0),  // 시즌 1
            LocalDateTime.of(2024, 7, 1, 12, 0),  // 시즌 2
    };

    private Integer season;
    private Boolean isMatched;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;

    public static HistoryResponseDTO of(BlindDate blindDate) {
        return builder()
                .season(blindDate.getSeason())
                .isMatched(Objects.nonNull(blindDate.getMatchingBlindDateId()))
                .date(seasonDate[blindDate.getSeason()])
                .build();
    }
}
