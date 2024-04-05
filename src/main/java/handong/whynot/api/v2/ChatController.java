package handong.whynot.api.v2;

import handong.whynot.dto.chat.ChatMessageDTO;
import handong.whynot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @MessageMapping("/{topicName}")
  @SendTo("/topic/{topicName}")
  public ChatMessageDTO greeting(@DestinationVariable String topicName, ChatMessageDTO message, Message<String> processedMessage) {
    Long accountId = chatService.getAccountIdByMessage(processedMessage);
    chatService.chatMessage(accountId, topicName, message);
    return new ChatMessageDTO(HtmlUtils.htmlEscape(message.getContent()));
  }
}
