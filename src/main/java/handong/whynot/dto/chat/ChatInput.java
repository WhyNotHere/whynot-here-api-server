package handong.whynot.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatInput {
  private String content;
  private String nickname;
  private String profileImg;
}
