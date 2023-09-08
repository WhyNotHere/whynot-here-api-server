package handong.whynot.repository;

import handong.whynot.domain.MatchingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {
  List<MatchingHistory> findAllBySeason(Integer season);
}