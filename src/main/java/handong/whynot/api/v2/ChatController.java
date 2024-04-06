package handong.whynot.api.v2;

import handong.whynot.domain.Account;
import handong.whynot.dto.chat.ChatInput;
import handong.whynot.dto.chat.ChatMessageDTO;
import handong.whynot.dto.chat.ChatRequestDTO;
import handong.whynot.service.AccountService;
import handong.whynot.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final AccountService accountService;

  @MessageMapping("/{topicName}")
  @SendTo("/topic/{topicName}")
  public ChatMessageDTO greeting(@DestinationVariable String topicName, ChatInput message, Message<String> processedMessage) {
    Long accountId = chatService.getAccountIdByMessage(processedMessage);
    chatService.chatMessage(accountId, topicName, message);
    return ChatMessageDTO.getRealTimeDTO(HtmlUtils.htmlEscape(message.getContent()), HtmlUtils.htmlEscape(message.getNickname()));
  }

  @Operation(summary = "채팅방 메세지 전체 조회")
  @PostMapping("/chat-messages")
  public List<ChatMessageDTO> getChatMessages(@RequestBody ChatRequestDTO request) {
    Account account = accountService.getCurrentAccount();
    return chatService.getChatMessages(account, request.getHashcode());
  }
}
