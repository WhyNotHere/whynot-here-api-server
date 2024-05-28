package handong.whynot.service;

import handong.whynot.domain.NoticeComment;
import handong.whynot.dto.blind_date.NoticeCommentResponseDTO;
import handong.whynot.repository.NoticeCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeCommentRepository noticeCommentRepository;

    public NoticeComment getLatestComment() {
        return noticeCommentRepository.findTopByOrderByCreatedDtDesc()
                .orElseGet(() -> NoticeComment.builder().title("").build());
    }

    public List<NoticeComment> getComments() {
        return noticeCommentRepository.findAllByOrderByCreatedDtDesc();
    }

    public NoticeComment getCommentById(Long id) {
        return noticeCommentRepository.findById(id).orElse(null);
    }
}
