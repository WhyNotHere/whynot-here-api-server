package handong.whynot.repository;

import handong.whynot.domain.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
    Optional<NoticeComment> findFirstByOrderByIdDesc();
}
