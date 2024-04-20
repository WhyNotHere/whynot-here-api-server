package handong.whynot.repository;

import handong.whynot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByHashcode(String topicName);

  Optional<ChatRoom> findByName(String name);
}
