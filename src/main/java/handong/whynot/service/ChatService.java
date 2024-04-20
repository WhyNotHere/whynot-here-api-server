package handong.whynot.service;

import handong.whynot.domain.Account;
import handong.whynot.domain.ChatMessage;
import handong.whynot.domain.ChatRoom;
import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.chat.ChatInput;
import handong.whynot.dto.chat.ChatMessageDTO;
import handong.whynot.dto.chat.ChatResponseCode;
import handong.whynot.exception.account.AccountNotFoundException;
import handong.whynot.exception.chat.ChatRoomNotFoundException;
import handong.whynot.exception.chat.InvalidParticipantException;
import handong.whynot.exception.chat.InvalidTokenException;
import handong.whynot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final ChatMessageRepository messageRepository;
  private final ChatRoomRepository roomRepository;
  private final ChatParticipantRepository participantRepository;
  private final AccountRepository accountRepository;

  @Async
  @Transactional
  public void chatMessage(Long accountId, String topicName, ChatInput message) {
    Account account = accountRepository.findById(accountId)
      .orElseThrow(() -> new AccountNotFoundException(AccountResponseCode.ACCOUNT_READ_FAIL));

    // 채팅방에 참여하고 있는지 검증
    if (! participantRepository.existsByAccount(account)) {
      throw new InvalidParticipantException(ChatResponseCode.CHAT_INVALID_PARTICIPANT);
    }

    ChatRoom chatRoom = roomRepository.findByHashcode(topicName)
      .orElseThrow(() -> new ChatRoomNotFoundException(ChatResponseCode.CHAT_ROOM_READ_FAIL));

    ChatMessage msg = ChatMessage.builder()
      .content(message.getContent())
      .account(account)
      .chatRoom(chatRoom)
      .build();
    messageRepository.save(msg);
  }

  public Long getAccountIdByMessage(Message<String> processedMessage) {
    try {
      return Long.parseLong(processedMessage.getHeaders().get("accountId").toString());
    } catch (Exception e) {
      throw new InvalidTokenException(ChatResponseCode.CHAT_INVALID_TOKEN);
    }
  }

  public List<ChatMessageDTO> getChatMessages(Account account, String hashcode) {
    // 채팅방에 참여하고 있는지 검증
    if (! participantRepository.existsByAccount(account)) {
      throw new InvalidParticipantException(ChatResponseCode.CHAT_INVALID_PARTICIPANT);
    }

    ChatRoom chatRoom = roomRepository.findByHashcode(hashcode)
      .orElseThrow(() -> new ChatRoomNotFoundException(ChatResponseCode.CHAT_ROOM_READ_FAIL));

    return messageRepository.findAllByChatRoom(chatRoom).stream()
      .map(ChatMessageDTO::of)
      .collect(Collectors.toList());
  }

  public String getChatNameBy(String name) {
    ChatRoom chatRoom = roomRepository.findByName(name)
      .orElseThrow(() -> new ChatRoomNotFoundException(ChatResponseCode.CHAT_ROOM_READ_FAIL));

    return chatRoom.getHashcode();
  }
}
