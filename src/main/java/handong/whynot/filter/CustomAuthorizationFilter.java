package handong.whynot.filter;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.account.UserAccount;
import handong.whynot.exception.account.AccountNotVerifiedException;
import handong.whynot.exception.account.AccountTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String authorization = request.getHeader("Authorization");

        try {
            // 인증 필요한 요청이 아닌 경우 bypass
            if (StringUtils.isNotBlank(authorization)) {

                SignedJWT signedJWT = SignedJWT.parse(authorization);

                // jwt Secret 검증
                signedJWT.verify(new MACVerifier(jwtSecret));

                // jwt ExpiredTime 검증
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
                LocalDateTime tokenExpiredTime = LocalDateTime.ofInstant(jwtClaimsSet.getExpirationTime().toInstant(), ZoneOffset.UTC);

                if (tokenExpiredTime.isBefore(LocalDateTime.now())) {  // 지금보다 과거이면 true
                    throw new AccountTokenExpiredException(AccountResponseCode.ACCOUNT_TOKEN_EXPIRED);
                }

                String accountId = jwtClaimsSet.getSubject();
                UserAccount userAccount = new UserAccount(accountId);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userAccount, null,
                          jwtClaimsSet.getStringListClaim("roles").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (AccountTokenExpiredException e) {
            // 만료된 토큰인 경우
            throw new AccountTokenExpiredException(AccountResponseCode.ACCOUNT_TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 비정상 토큰인 경우
            throw new AccountNotVerifiedException(AccountResponseCode.ACCOUNT_FORBIDDEN);
        }
    }
}