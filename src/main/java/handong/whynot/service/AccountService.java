package handong.whynot.service;

import handong.whynot.domain.Account;
import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.account.SignUpDTO;
import handong.whynot.dto.account.UserAccount;
import handong.whynot.exception.account.AccountAlreadyExistEmailException;
import handong.whynot.exception.account.AccountAlreadyExistNicknameException;
import handong.whynot.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    @Transactional
    public Account createAccount(SignUpDTO signUpDTO) {

        // 중복 검증 (이메일)
        if (accountRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new AccountAlreadyExistEmailException(AccountResponseCode.ACCOUNT_ALREADY_EXIST_EMAIL);
        }
        // 중복 검증 (닉네임)
        if (accountRepository.existsByNickname(signUpDTO.getNickname())) {
            throw new AccountAlreadyExistNicknameException(AccountResponseCode.ACCOUNT_ALREADY_EXIST_NICKNAME);
        }

        Account account = Account.builder()
                .email(signUpDTO.getEmail())
                .nickname(signUpDTO.getNickname())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .build();
        Account newAccount = accountRepository.save(account);

        // todo: 이메일 확인을 위한 토큰 생성 필요
//        newAccount.generateEmailCheckToken();

        // todo: 이메일 전송
//        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}