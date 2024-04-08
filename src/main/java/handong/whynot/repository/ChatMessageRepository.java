package handong.whynot.repository;

import handong.whynot.domain.ChatMessage;
import handong.whynot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
