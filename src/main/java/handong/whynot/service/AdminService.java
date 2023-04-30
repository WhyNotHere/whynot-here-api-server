package handong.whynot.service;

import handong.whynot.domain.Account;
import handong.whynot.domain.Role;
import handong.whynot.domain.StudentAuth;
import handong.whynot.dto.account.AccountResponseCode;
import handong.whynot.dto.account.AdminApproveRequestDTO;
import handong.whynot.dto.account.StudentAuthRequestDTO;
import handong.whynot.dto.admin.AdminResponseCode;
import handong.whynot.dto.admin.AdminStudentAuthResponseDTO;
import handong.whynot.exception.account.AccountNotFoundException;
import handong.whynot.exception.account.StudentAuthNotFoundException;
import handong.whynot.repository.AccountRepository;
import handong.whynot.repository.StudentAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final StudentAuthRepository studentAuthRepository;
  private final AccountRepository accountRepository;
  private final RoleService roleService;

  public void requestStudentAuth(StudentAuthRequestDTO dto, Account account) {

    StudentAuth studentAuth = StudentAuth.builder()
      .account(account)
      .imgUrl(dto.getImgUrl())
      .isAuthenticated(false)
      .build();

    studentAuthRepository.save(studentAuth);
  }

  @Transactional
  public void updateStudentAuth(StudentAuthRequestDTO dto, Account account) {
    StudentAuth studentAuth = studentAuthRepository.findByAccountId(account.getId())
      .orElseThrow(() -> new StudentAuthNotFoundException(AdminResponseCode.STUDENT_AUTH_NOT_FOUND));

    studentAuth.updateImageUrl(dto.getImgUrl());
  }

  public StudentAuth getStudentImg(Account account) {
    return studentAuthRepository.findByAccountId(account.getId())
      .orElseThrow(() -> new StudentAuthNotFoundException(AdminResponseCode.STUDENT_AUTH_NOT_FOUND));
  }

  public List<AdminStudentAuthResponseDTO> getRequests() {
    return studentAuthRepository.findAll().stream()
      .map(AdminStudentAuthResponseDTO::of)
      .collect(Collectors.toList());
  }

  @Transactional
  public void approveRequests(List<AdminApproveRequestDTO> approveList) {
    for (AdminApproveRequestDTO approve: approveList) {
      StudentAuth studentAuth = studentAuthRepository.findByAccountId(approve.getAccountId())
        .orElseThrow(() -> new StudentAuthNotFoundException(AdminResponseCode.STUDENT_AUTH_NOT_FOUND));
      studentAuth.updateIsAuthenticated(true);

      Account account = accountRepository.findById(approve.getAccountId())
        .orElseThrow(() -> new AccountNotFoundException(AccountResponseCode.ACCOUNT_READ_FAIL));
      account.approveStudentAuth(approve.getStudentId(), approve.getStudentName());

      final Role role = roleService.getRoleByCode("ROLE_USER");
      account.addAccountRole(role);
    }
  }
}