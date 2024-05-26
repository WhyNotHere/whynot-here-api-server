package handong.whynot.service;

import handong.whynot.domain.NoticeComment;
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

    public String getOneComment() {
        return noticeCommentRepository.findTopByOrderByCreatedDtDesc()
                .orElseGet(() -> NoticeComment.builder().title("").build())
                .getTitle();
    }

    public List<NoticeComment> getComments() {
        return noticeCommentRepository.findAllByOrderByCreatedDtDesc();
    }
}
