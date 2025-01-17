package handong.whynot.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import handong.whynot.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

  private String content;
  private String nickname;
  private String profileImg;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN, timezone = "Asia/Seoul")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdDt;

  public static ChatMessageDTO of(ChatMessage message) {
    return builder()
      .content(message.getContent())
      .nickname(message.getAccount().getNickname())
      .profileImg(message.getAccount().getProfileImg())
      .createdDt(message.getCreatedDt())
      .build();
  }

  public static ChatMessageDTO getRealTimeDTO(ChatInput message) {
    return ChatMessageDTO.builder()
      .content(HtmlUtils.htmlEscape(message.getContent()))
      .nickname(HtmlUtils.htmlEscape(message.getNickname()))
      .profileImg(HtmlUtils.htmlEscape(message.getProfileImg()))
      .createdDt(LocalDateTime.now())
      .build();
  }
}
