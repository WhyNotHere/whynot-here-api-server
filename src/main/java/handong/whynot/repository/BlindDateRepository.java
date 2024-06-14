package handong.whynot.repository;

import handong.whynot.domain.Account;
import handong.whynot.domain.BlindDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlindDateRepository extends JpaRepository<BlindDate, Long> {
    Optional<BlindDate> findByAccountAndSeason(Account account, Integer season);

    Long countBySeason(Integer season);

    Boolean existsByAccountAndSeason(Account account, Integer season);

    List<BlindDate> findAllBySeason(Integer season);

    List<BlindDate> findAllBySeasonAndIsRetry(Integer season, boolean isRetry);

    List<BlindDate> findAllByAccountId(Long id);
}
