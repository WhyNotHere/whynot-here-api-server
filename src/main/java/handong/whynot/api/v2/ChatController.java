package handong.whynot.api.v2;

import handong.whynot.dto.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class ChatController {

  @MessageMapping("/{topicName}")
  @SendTo("/topic/{topicName}")
  public ChatMessage greeting(@DestinationVariable String topicName, ChatMessage message) {
    return new ChatMessage(HtmlUtils.htmlEscape(message.getContent()));
  }
}
