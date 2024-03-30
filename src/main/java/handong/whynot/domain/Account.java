package handong.whynot.domain;

import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.account.AccountResponseDTO;
import handong.whynot.exception.account.AccountAlreadyExistRoleException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "email_check_token")
    private String emailCheckToken;

    @Column(name = "email_check_token_generated_at")
    private LocalDateTime emailCheckTokenGeneratedAt;

    @Column(name = "auth_type")
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "category_order")
    private String categoryOrder;

    @Column(name = "oauth_ci")
    private String oauthCI;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "is_authenticated")
    private boolean isAuthenticated;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "blind_date_push")
    private boolean blindDatePush;

    @Column(name = "student_type")
    private String studentType;

    @Column(name = "sms_check_token")
    private String smsCheckToken;

    @Column(name = "sms_check_token_generated_at")
    private LocalDateTime smsCheckTokenGeneratedAt;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sms_sid")
    private String smsSid;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AccountRole> userRoleList = new ArrayList<>();

    public List<Role> getRoles() {
        return userRoleList.stream()
          .map(AccountRole::getRole)
          .collect(Collectors.toList());
    }

    public List<SimpleGrantedAuthority> getAuthorityList() {
        return userRoleList.stream()
          .map(AccountRole::getRole)
          .map(Role::getCode)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }

    public void generateEmailCheckToken() {
        emailCheckToken = UUID.randomUUID().toString().split("-")[0];
        emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        emailVerified = true;
        joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return emailCheckToken.equals(token);
    }

    public AccountResponseDTO getAccountDTO() {
        return AccountResponseDTO.of(this);
    }

    public Account addAccountRole(Role role) {
        if (hasRoleByCode(role.getCode())) {
            throw new AccountAlreadyExistRoleException(AccountResponseCode.ALREADY_EXIST_ROLE);
        }

        userRoleList.add(new AccountRole(this, role));
        return this;
    }

    public Boolean hasRoleByCode(String roleCode) {
        return userRoleList.stream()
          .anyMatch(it -> StringUtils.equals(it.getRole().getCode(), roleCode));
    }

    public void approveStudentAuth(Integer studentId, String studentName, String studentType) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentType = studentType;
        this.isAuthenticated = true;
    }

    public String getDisplayRole() {
        List<String> roles = getRoles().stream().map(Role::getCode).collect(Collectors.toList());
        if (roles.contains("ROLE_ADMIN")) return "관리자";
        else if (roles.contains("ROLE_MANAGER")) return "외부 매니저";
        else if (roles.contains("ROLE_USER")) return "";
        else return "미인증";
    }

    public void generateSMSCode(String code, String phoneNumber) {
        smsCheckToken = code;
        emailCheckTokenGeneratedAt = LocalDateTime.now();
        this.phoneNumber = phoneNumber;
    }
}
