package handong.whynot.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import handong.whynot.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatParticipantQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final QAccount qAccount = QAccount.account;
    private final QChatParticipant qChatParticipant = QChatParticipant.chatParticipant;

    public Map<Long, String> getIdNicknameMap(String hashcode) {
        List<Tuple> tuples = queryFactory.select(qAccount.id, qAccount.nickname)
          .from(qChatParticipant)
          .where(qChatParticipant.hashcode.eq(hashcode))
          .fetch();

        Map<Long, String> resultMap = new HashMap<>();
        for (Tuple tuple : tuples) {
            Long id = tuple.get(qAccount.id);
            String nickname = tuple.get(qAccount.nickname);
            resultMap.put(id, nickname);
        }

        return resultMap;
    }
}
