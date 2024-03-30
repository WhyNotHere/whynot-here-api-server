package handong.whynot.dto.account;

import handong.whynot.domain.Account;
import handong.whynot.domain.AuthType;
import handong.whynot.domain.Role;
import handong.whynot.domain.StudentType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private AuthType authType;
    private List<String> roles;
    private boolean isAuthenticated;
    private String studentType;
    private Boolean smsVerified;

    public static AccountResponseDTO of(Account account) {
        return builder()
                .id(account.getId())
                .email(account.getEmail())
                .nickname(account.getNickname())
                .profileImg(account.getProfileImg())
                .authType(account.getAuthType())
                .roles(account.getRoles().stream().map(Role::getCode).collect(Collectors.toList()))
                .isAuthenticated(account.isAuthenticated())
                .smsVerified(account.getSmsVerified())
                .studentType(account.getStudentType())
                .build();
    }
}
