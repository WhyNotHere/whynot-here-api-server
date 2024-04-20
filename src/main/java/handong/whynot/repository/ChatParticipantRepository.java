package handong.whynot.repository;

import handong.whynot.domain.Account;
import handong.whynot.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
  boolean existsByAccount(Account account);
}
