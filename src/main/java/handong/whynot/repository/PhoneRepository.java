package handong.whynot.repository;

import handong.whynot.domain.Account;
import handong.whynot.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByAccount(Account account);
}
