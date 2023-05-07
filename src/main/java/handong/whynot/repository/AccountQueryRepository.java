package handong.whynot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import handong.whynot.domain.Account;
import handong.whynot.domain.AuthType;
import handong.whynot.domain.QAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepository {

    private final JPAQueryFactory queryFactory;

    private final QAccount qAccount = QAccount.account;

    public Account findByVerifiedEmail(String email) {
        return queryFactory.selectFrom(qAccount)
                .where(qAccount.email.eq(email)
                        .and(qAccount.emailVerified.isTrue())
                        .and(qAccount.authType.eq(AuthType.local)
                          .or(qAccount.authType.eq(AuthType.admin))))
                .fetchOne();
    }

    public Account findByVerifiedNickname(String nickname) {
        return queryFactory.selectFrom(qAccount)
                .where(qAccount.nickname.eq(nickname)
                        .and(qAccount.emailVerified.isTrue())
                        .and(qAccount.authType.eq(AuthType.local)
                          .or(qAccount.authType.eq(AuthType.admin))))
                .fetchOne();
    }

    public Account findByEmail(String email) {
        return queryFactory.selectFrom(qAccount)
          .where(qAccount.email.eq(email)
            .and(qAccount.authType.eq(AuthType.local)))
          .fetchOne();
    }

    public Account findByAdminEmail(String email) {
        return queryFactory.selectFrom(qAccount)
          .where(qAccount.email.eq(email)
            .and(qAccount.authType.eq(AuthType.admin)))
          .fetchOne();
    }
}
