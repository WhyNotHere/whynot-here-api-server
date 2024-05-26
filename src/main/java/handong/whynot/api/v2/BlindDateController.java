package handong.whynot.api.v2;

import handong.whynot.domain.Account;
import handong.whynot.domain.BlindDateSummary;
import handong.whynot.dto.blind_date.*;
import handong.whynot.dto.blind_date.enums.GBlindDateState;
import handong.whynot.dto.common.ResponseDTO;
import handong.whynot.service.AccountService;
import handong.whynot.service.BlindDateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/blind-date")
public class BlindDateController {

  private final BlindDateService blindDateService;
  private final AccountService accountService;

  @Operation(summary = "[졸업생] 소개팅 지원 (약관동의)")
  @PostMapping("")
  @ResponseStatus(CREATED)
  public ResponseDTO createBlindDateBySeason(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    blindDateService.createBlindDate(season, account);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_CREATED_OK);
  }

  @Operation(summary = "[졸업생] 소개팅 수정")
  @PutMapping("")
  public ResponseDTO updateBlindDate(@RequestBody BlindDateRequestDTO request) {
    Account account = accountService.getCurrentAccount();
    blindDateService.updateBlindDate(request, account);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_UPDATED_OK);
  }

  @Operation(summary = "[졸업생] 소개팅 지원 삭제")
  @DeleteMapping("")
  public ResponseDTO deleteBlindDateBySeason(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    blindDateService.deleteBlindDateBySeason(account, season);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_DELETED_OK);
  }

  @Operation(summary = "[졸업생] 소개팅 최종 제출")
  @PutMapping("/finish")
  public ResponseDTO finishEditingBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();
    blindDateService.finishEditing(account, season);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_APPLY_FINISH_OK);
  }

  @Operation(summary = "[졸업생] 참여비 납부 동의")
  @PostMapping("/g-fee")
  public ResponseDTO createGBlindDateFee(@RequestBody BlindDateFeeRequestDTO dto) {
    Account account = accountService.getCurrentAccount();
    blindDateService.createGBlindDateFee(account, dto);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_FEE_CREATED_OK);
  }

  @Operation(summary = "[졸업생] 매칭 후 상대방 정보 조회")
  @GetMapping("/matching-result")
  public BlindDateMatchingResponseDTO getMatchingResultBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();
    return blindDateService.getMatchingResultBySeason(season, account);
  }

  @Operation(summary = "[졸업생] 재매칭 신청")
  @PutMapping("/retry")
  public ResponseDTO applyRetryBySeason(@RequestParam Integer season,
                                        @RequestBody BlindDateRetryRequestDTO request) {
    Account account = accountService.getCurrentAccount();
    blindDateService.applyRetryBySeason(account, season, request.getReason());

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_RETRY_APPLY_OK);
  }

  @Operation(summary = "[졸업생] 참여비 즉시 환불 or 재매칭 신청")
  @PutMapping("/g-fee/recall")
  public ResponseDTO requestRecallFee(@RequestBody RecallRequestDTO recall) {

    Account account = accountService.getCurrentAccount();
    blindDateService.requestRecallFee(account, recall);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_FEE_RECALL_OK);
  }

  @Operation(summary = "[졸업생] 비매너 신고")
  @PutMapping("/manners")
  public ResponseDTO reportMannersBySeason(@RequestParam Integer season,
                                           @RequestBody MannerReportRequestDTO request) {
    Account account = accountService.getCurrentAccount();
    blindDateService.reportManners(account, season, request);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_REPORT_MANNERS_OK);
  }

  @Operation(summary = "[졸업생] 정보 입력 단계 조회 (본인, 상대방)")
  @GetMapping("/steps")
  public BlindDateStepResponseDTO getStepsBySeason(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    return blindDateService.getSteps(account, season);
  }

  @Operation(summary = "[졸업생] 본인 입력 정보 조회")
  @GetMapping("/my-apply")
  public BlindDateResponseDTO getMyApplyBySeason(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    return blindDateService.getMyApply(account, season);
  }

  @Operation(summary = "[졸업생] 현재 상태 조회")
  @GetMapping("/g-state")
  public GBlindDateState getGBlindDateState(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    return blindDateService.getGBlindDateState(account, season);
  }

  @Operation(summary = "[졸업생] (매칭 실패 후) 재매칭 여부 신청")
  @PutMapping("/g-rematch")
  public ResponseDTO requestRematch(@RequestBody ReMatchRequestDTO request) {

    Account account = accountService.getCurrentAccount();
    blindDateService.requestRematch(account, request);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_REMATCH_OK);
  }

  @Operation(summary = "[졸업생] 추가 정보 입력")
  @PutMapping("/g-extra-info")
  public ResponseDTO requestExtraInfo(@RequestBody GExtraInfo request, @RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    blindDateService.requestExtraInfo(account, request, season);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_EXTRA_INFO_OK);
  }

  @Operation(summary = "[졸업생] 추가 정보 조회")
  @GetMapping("/g-extra-info")
  public GExtraInfo getExtraInfo(@RequestParam Integer season) {

    Account account = accountService.getCurrentAccount();
    return blindDateService.getExtraInfo(account, season);
  }

  @Operation(summary = "소개팅 지원자 카운트 조회")
  @GetMapping("/total-cnt")
  public Long getApplicantsCntBySeason(@RequestParam Integer season) {
    return blindDateService.getApplicantsCntBySeason(season);
  }

  @Operation(summary = "방문자수 조회")
  @GetMapping("/visit-cnt")
  public Integer getVisitCnt(@RequestParam Integer season) {
    return blindDateService.getVisitCnt(season);
  }

  @Operation(summary = "소개팅 지원 여부 조회")
  @GetMapping("/participation")
  public Boolean getIsParticipatedBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();
    return blindDateService.getIsParticipatedBySeason(season, account);
  }

  @Operation(summary = "매칭 결과 노출 여부 조회")
  @GetMapping("/reveal-result")
  public Boolean getIsRevealResultBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();
    return blindDateService.getIsRevealResultBySeason(season, account);
  }

  @Operation(summary = "매칭 결과 summary 조회")
  @GetMapping("/summary")
  public BlindDateSummary getMatchingResultSummary(@RequestParam Integer season) {

    return blindDateService.getMatchingResultSummary(season);
  }

  @Operation(summary = "보증금 납부 동의")
  @PostMapping("/fee")
  public ResponseDTO createBlindDateFee(@RequestBody BlindDateFeeRequestDTO dto) {
    Account account = accountService.getCurrentAccount();
    blindDateService.createBlindDateFee(account, dto);

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_FEE_CREATED_OK);
  }

  @Operation(summary = "보증금 납부 확인 여부 조회")
  @GetMapping("/fee/confirm")
  public Boolean getIsSubmittedBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();
    return blindDateService.getFeeIsSubmitted(account, season);
  }

  @Operation(summary = "소개팅 최종 제출 여부 조회")
  @GetMapping("/finish")
  public Boolean getBlindDateSubmittedBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();

    return blindDateService.getBlindDateSubmitted(account, season);
  }

  @Operation(summary = "만남 인증 사진 업로드")
  @PutMapping("/matching/image")
  public ResponseDTO createMatchingImageBySeason(@RequestParam Integer season,
                                                 @RequestBody MatchingImageRequestDTO request) {
    Account account = accountService.getCurrentAccount();
    blindDateService.createMatchingImage(account, season, request.getImageLink());

    return ResponseDTO.of(BlindDateResponseCode.BLIND_DATE_MATCHING_IMAGE_CREATED_OK);
  }

  @Operation(summary = "만남 인증 사진 조회")
  @GetMapping("/matching/image")
  public MatchingImageResponseDTO getMatchingImageBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();

    return blindDateService.getMatchingImageBySeason(account, season);
  }

  @Operation(summary = "만남 인증 승인 여부 조회")
  @GetMapping("/matching/confirm")
  public Boolean getMatchingApprovedBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();

    return blindDateService.getMatchingApprovedBySeason(account, season);
  }

  @Operation(summary = "재매칭 신청 여부 조회")
  @GetMapping("/retry")
  public Boolean getIsRetryBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();

    return blindDateService.getIsRetryBySeason(account, season);
  }

  @Operation(summary = "비매너 신고 여부 조회")
  @GetMapping("/manners")
  public Boolean getIsReportMannersBySeason(@RequestParam Integer season) {
    Account account = accountService.getCurrentAccount();

    return blindDateService.getIsReportMannersBySeason(account, season);
  }

  @Operation(summary = "공지 한 줄 멘트 조회")
  @GetMapping("/notice-comment")
  public String getNoticeComment() {
    return blindDateService.getNoticeComment();
  }

  @Operation(summary = "실시간 정보 조회")
  @GetMapping("/real-data")
  public RealDataResponseDTO getRealData(@RequestParam Integer season) {
    return blindDateService.getRealData(season);
  }
}
