package handong.whynot.dto.blind_date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import handong.whynot.domain.NoticeComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeCommentResponseDTO {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private String title;
    private String content;
    private String imgLink;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDt;

    public static NoticeCommentResponseDTO of(NoticeComment comment) {
        return builder()
                .title(comment.getTitle())
                .content(comment.getContent())
                .imgLink(comment.getImgLink())
                .createdDt(comment.getCreatedDt())
                .build();
    }
}
