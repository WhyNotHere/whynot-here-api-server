package handong.whynot.service;

import handong.whynot.domain.*;
import handong.whynot.dto.admin.AdminBlindDateResponseDTO;
import handong.whynot.dto.blind_date.*;
import handong.whynot.dto.blind_date.enums.GBlindDateState;
import handong.whynot.dto.friend_meeting.FriendMeetingResponseCode;
import handong.whynot.exception.blind_date.*;
import handong.whynot.exception.friend_meeting.FriendMeetingDuplicatedException;
import handong.whynot.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlindDateService {

  private final BlindDateRepository blindDateRepository;
  private final BlindDateForCacheRepository blindDateForCacheRepository;
  private final ExcludeCondRepository excludeCondRepository;
  private final MobilePushService mobilePushService;
  private final MatchingHistoryRepository matchingHistoryRepository;
  private final MatchingHistoryService matchingHistoryService;
  private final BlindDateSummaryRepository blindDateSummaryRepository;
  private final BlindDateFeeRepository blindDateFeeRepository;
  private final BlindDateImageLinkRepository blindDateImageLinkRepository;
  private final FriendMeetingRepository friendMeetingRepository;
  private final BlindDateMatchingHelperRepository blindDateMatchingHelperRepository;
  private final AccountQueryRepository accountQueryRepository;
  private final NoticeCommentRepository noticeCommentRepository;

  @Transactional
  public void createBlindDate(Integer season, Account account) {
    // 1. 학생증 인증이 된 사용자인지 확인
    if (! account.isAuthenticated()) {
      throw new BlindDateNotAuthenticatedException(BlindDateResponseCode.BLIND_DATE_NOT_AUTHENTICATED);
    };

    // 2. (진행 중인 시즌 기간) 소개팅에 지원한 적이 있는지 확인
    if (isDuplicatedApply(account, season)) {
      throw new BlindDateDuplicatedException(BlindDateResponseCode.BLIND_DATE_DUPLICATED);
    }

    // 3. 소개팅 지원
    BlindDate blindDate = BlindDate.of(season, account);
    blindDateRepository.save(blindDate);
  }

  @Transactional
  public void updateBlindDate(BlindDateRequestDTO request, Account account) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, request.getSeason())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.updateBlindDate(request);

    // 1. exclude condition 저장
    saveExcludeConds(blindDate, request.getExcludeCondList());

    // 2. image links 저장
    saveImageLinks(account, blindDate, request.getImageLinks());
  }

  private void saveExcludeConds(BlindDate blindDate, List<ExcludeCond> excludeCondList) {
    excludeCondRepository.deleteAllByBlindDate(blindDate);

    List<ExcludeCond> excludeConds = excludeCondList.stream()
      .filter(this::isNotEmptyCond)
      .peek(it -> it.setBlindDate(blindDate))
      .collect(Collectors.toList());
    excludeCondRepository.saveAll(excludeConds);
  }

  private boolean isNotEmptyCond(ExcludeCond cond) {
    return StringUtils.isNotBlank(cond.getName()) ||
      StringUtils.isNotBlank(cond.getDepartment()) ||
      StringUtils.isNotBlank(cond.getStudentId());
  }

  private void saveImageLinks(Account account, BlindDate blindDate, List<String> links) {
    blindDateImageLinkRepository.deleteAllByBlindDate(blindDate);

    List<BlindDateImageLink> imageLinks = links.stream()
      .map(it -> BlindDateImageLink.of(account.getId(), blindDate, it))
      .collect(Collectors.toList());
    blindDateImageLinkRepository.saveAll(imageLinks);
  }

  private Boolean isDuplicatedApply(Account account, Integer season) {
    Optional<BlindDate> blindDate = blindDateRepository.findByAccountAndSeason(account, season);
    return blindDate.isPresent();
  }

  public Long getApplicantsCntBySeason(Integer season) {
    return blindDateRepository.countBySeason(season);
  }

  public Boolean getIsParticipatedBySeason(Integer season, Account account) {

    return blindDateFeeRepository.existsByAccountIdAndSeasonAndUseYn(account.getId(), season, "Y");
  }

  public BlindDateMatchingResponseDTO getMatchingResultBySeason(Integer season, Account account) {
    // 소개팅 지원 확인
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    // 매칭 대상 여부 확인
    if (Objects.isNull(blindDate.getMatchingBlindDateId())) {
      throw new BlindDateNotMatchedException(BlindDateResponseCode.BLIND_DATE_NOT_MATCHED);
    }

    BlindDate matchedBlindDate = blindDateRepository.findById(blindDate.getMatchingBlindDateId())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    // reveal 여부 확인
    if (! blindDate.getIsReveal()) {
      throw new BlindDateNotOpenedException(BlindDateResponseCode.REVEAL_FAIL);
    }

   // 상대방 이름 한글자 masking
    if (matchedBlindDate.getName().length() >= 2) {
      matchedBlindDate.setName(matchedBlindDate.getName().charAt(0) + "*" + matchedBlindDate.getName().substring(2));
    }

    // 상대방 이미지 조회
    List<String> images = blindDateImageLinkRepository.findAllByBlindDate(matchedBlindDate).stream()
      .map(BlindDateImageLink::getLink)
      .collect(Collectors.toList());

    return BlindDateMatchingResponseDTO.of(matchedBlindDate, blindDate.getName(), images);
  }

  @Transactional
  public void createMatching(Long maleId, Long femaleId) {
    // 남자인지 확인
    BlindDate male = blindDateRepository.findById(maleId)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));
    if (! StringUtils.equals(male.getGender(), "M")) {
      throw new InvalidMatchingException(BlindDateResponseCode.MATCHING_INVALID);
    }

    // 여자인지 확인
    BlindDate female = blindDateRepository.findById(femaleId)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));
    if (! StringUtils.equals(female.getGender(), "F")) {
      throw new InvalidMatchingException(BlindDateResponseCode.MATCHING_INVALID);
    }

    // 이미 매칭이 된 경우
    if (Objects.nonNull(male.getMatchingBlindDateId())) {
      throw new InvalidMatchingException(BlindDateResponseCode.MATCHING_INVALID);
    }

    // 매칭 업데이트
    male.updateMatchingBlindDate(femaleId);
    female.updateMatchingBlindDate(maleId);

    MatchingHistory history = MatchingHistory.builder()
      .maleId(maleId)
      .femaleId(femaleId)
      .season(male.getSeason())
      .isApproved(false)
      .isRetry(false)
      .build();
    matchingHistoryRepository.save(history);
  }

  public List<AdminBlindDateResponseDTO> getBlindDateListBySeason(Integer season) {
    List<AdminBlindDateResponseDTO> responseList = blindDateRepository.findAllBySeason(season).stream()
      .filter(BlindDate::getIsPayed)
      .map(AdminBlindDateResponseDTO::of)
      .collect(Collectors.toList());

    HashMap<Long, List<Long>> baseMap = getBlindDateBaseMatching(season);
    List<Long> empty = new ArrayList<>();
    for (AdminBlindDateResponseDTO blindDate : responseList) {
      blindDate.setBaseMatching(baseMap.getOrDefault(blindDate.getBlindDateId(), empty));
    }

    return responseList;
  }

  public List<AdminBlindDateResponseDTO> getGBlindDateListBySeason(Integer season) {

    return blindDateRepository.findAllBySeason(season).stream()
      .filter(it -> Objects.equals(it.getGState(), GBlindDateState.SCREEN))
      .map(AdminBlindDateResponseDTO::of)
      .collect(Collectors.toList());
  }

  public List<String> getGBlindDateImage(Long blindId) {
    BlindDate blindDate = blindDateRepository.findById(blindId)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    return blindDateImageLinkRepository.findAllByBlindDate(blindDate).stream()
      .map(BlindDateImageLink::getLink)
      .collect(Collectors.toList());
  }

  public void noticeMatchingInfoBySeason(Integer season) {
    List<BlindDate> blindDateList = blindDateRepository.findAllBySeason(season);
    List<BlindDate> filteredList = blindDateList.stream()
      .filter(BlindDate::getIsPayed)
      .collect(Collectors.toList());
    noticeResult(filteredList);
  }

  public void notice2MatchingInfoBySeason(Integer season) {
    List<BlindDate> blindDateList = blindDateRepository.findAllBySeason(season).stream()
      .filter(it -> Objects.equals(it.getGState(), GBlindDateState.REMATCH))
      .collect(Collectors.toList());
    noticeResult2(blindDateList);
  }

  public void noticeRetryBySeason(Integer season) {
    List<BlindDate> blindDateList = blindDateRepository.findAllBySeasonAndIsRetry(season, true);
    notice2Result(blindDateList);
  }

  private void noticeResult(List<BlindDate> blindDateList) {
    // 사용자 매칭 결과 노출 ON
    for (BlindDate blindDate : blindDateList) {
      blindDate.setIsReveal(true);
    }

    // 매칭 성공 사용자 push
    List<BlindDate> successList = blindDateList.stream()
      .filter(it -> Objects.nonNull(it.getMatchingBlindDateId()))
      .peek(it -> it.setGState(GBlindDateState.MATCH_OK))
      .collect(Collectors.toList());

    List<Account> accountSuccessList = successList.stream()
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingSuccess(accountSuccessList);

    // 매칭 실패한 사용자 push
    List<BlindDate> failList = blindDateList.stream()
      .filter(it -> Objects.isNull(it.getMatchingBlindDateId()))
      .peek(it -> it.setGState(GBlindDateState.MATCH_FAIL))
      .collect(Collectors.toList());

    List<Account> accountFailList = failList.stream()
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingFail(accountFailList);
  }

  private void noticeResult2(List<BlindDate> blindDateList) {
    // 사용자 매칭 결과 노출 ON
    for (BlindDate blindDate : blindDateList) {
      blindDate.setIsReveal2(true);
    }

    // 매칭 성공 사용자 push
    List<BlindDate> successList = blindDateList.stream()
      .filter(it -> Objects.nonNull(it.getMatchingBlindDateId()))
      .peek(it -> it.setGState(GBlindDateState.REMATCH_OK))
      .collect(Collectors.toList());

    List<Account> accountSuccessList = successList.stream()
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingSuccess(accountSuccessList);

    // 매칭 실패한 사용자 push
    List<BlindDate> failList = blindDateList.stream()
      .filter(it -> Objects.isNull(it.getMatchingBlindDateId()))
      .peek(it -> it.setGState(GBlindDateState.REMATCH_OK))
      .collect(Collectors.toList());

    List<Account> accountFailList = failList.stream()
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingFail2(accountFailList);
  }

  private void notice2Result(List<BlindDate> blindDateList) {
    // 사용자 매칭 결과 노출 ON
    for (BlindDate blindDate : blindDateList) {
      blindDate.setIsReveal2(true);
      blindDate.setGState(GBlindDateState.REMATCH_OK);
    }

    // 2차 매칭 성공 사용자 push
    List<Account> accountSuccessList = blindDateList.stream()
      .filter(it -> Objects.nonNull(it.getMatchingBlindDateId()))
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingSuccess(accountSuccessList);

    // 2차 매칭 실패한 사용자 push
    List<Account> accountFailList = blindDateList.stream()
      .filter(it -> Objects.isNull(it.getMatchingBlindDateId()))
      .map(BlindDate::getAccount)
      .collect(Collectors.toList());

    mobilePushService.pushMatchingFail(accountFailList);
  }

  public Boolean getIsRevealResultBySeason(Integer season, Account account) {
    List<Long> accountList = blindDateForCacheRepository.getMatchedAccountListByCache(season);

    return accountList.contains(account.getId());
  }

  public BlindDateSummary getMatchingResultSummary(Integer season) {
    return blindDateSummaryRepository.findBySeason(season);
  }

  @Transactional
  public Integer getVisitCnt(Integer season) {
    BlindDateSummary summary = blindDateSummaryRepository.findBySeason(season);
    summary.setTotalCnt(summary.getTotalCnt()+1);
    return summary.getTotalCnt();
  }

  @Transactional
  public void createBlindDateFee(Account account, BlindDateFeeRequestDTO dto) {
    // 신청한 내역이 있는지 확인
    if (isDuplicatedBlindDateFee(account, dto.getSeason())) {
      throw new BlindDateFeeDuplicatedException(BlindDateResponseCode.BLIND_DATE_FEE_DUPLICATED);
    }

    // 친구 만남 신청한 내역이 있는지 확인
    if (isDuplicatedFriendMeeting(account, dto.getSeason())) {
      throw new FriendMeetingDuplicatedException(FriendMeetingResponseCode.FRIEND_MEETING_DUPLICATED);
    }

    BlindDateFee dateFee = BlindDateFee.of(account.getId(), dto);
    blindDateFeeRepository.save(dateFee);
  }

  @Transactional
  public void createGBlindDateFee(Account account, BlindDateFeeRequestDTO dto) {
    // 졸업생 소개팅 지원한 사용자들에 한해서 참가비 납부 정보 획득
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, dto.getSeason())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    BlindDateFee dateFee = BlindDateFee.of(account.getId(), blindDate.getId(), dto);
    blindDateFeeRepository.save(dateFee);

    // 상태 업데이트
    blindDate.setGState(GBlindDateState.FEE);
  }

  private Boolean isDuplicatedBlindDateFee(Account account, Integer season) {
    Optional<BlindDateFee> blindDateFee = blindDateFeeRepository.findByAccountIdAndSeasonAndUseYn(account.getId(), season, "Y");
    return blindDateFee.isPresent();
  }

  private Boolean isDuplicatedFriendMeeting(Account account, Integer season) {
    Optional<FriendMeeting> friendMeeting = friendMeetingRepository.findByAccountAndSeason(account, season);
    return friendMeeting.isPresent();
  }

  public Boolean getFeeIsSubmitted(Account account, Integer season) {

    BlindDateFee blindDateFee = blindDateFeeRepository.findByAccountIdAndSeasonAndUseYn(account.getId(), season, "Y")
      .orElseGet(BlindDateFee::new);
    return blindDateFee.getIsSubmitted();
  }

  public Boolean getBlindDateSubmitted(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseGet(BlindDate::new);

    return blindDate.getIsSubmitted();
  }

  @Transactional
  public void finishEditing(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.setIsSubmitted(true);
    blindDate.setGState(GBlindDateState.SCREEN);
  }

  @Transactional
  public void createMatchingImage(Account account, Integer season, String link) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    matchingHistoryService.updateImageLink(blindDate, link);
  }

  public MatchingImageResponseDTO getMatchingImageBySeason(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    String imageLink = matchingHistoryService.getMatchingImageByBlindDate(blindDate);
    return MatchingImageResponseDTO.builder()
      .imageLink(imageLink)
      .build();
  }

  public Boolean getMatchingApprovedBySeason(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    MatchingHistory matchingHistory = matchingHistoryService.getMatchingHistoryByBlindDate(blindDate);
    return matchingHistory.getIsApproved();
  }

  @Transactional
  public void applyRetryBySeason(Account account, Integer season, String reason) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));
    blindDate.setRetryReason(reason);

    BlindDate matched = blindDateRepository.findById(blindDate.getMatchingBlindDateId())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    // 상태 업데이트
    updateRetryState(blindDate, matched);   // 본인, 싱대방

    // 매칭 내역 재매칭으로 수정
    MatchingHistory matchingHistory = matchingHistoryService.getMatchingHistoryByBlindDate(blindDate);
    matchingHistory.setIsRetry(true);

    // 상대방 재매칭 대상자 안내 (푸시 알림)
    List<Account> accountList = Collections.singletonList(matched.getAccount());
    mobilePushService.pushIsRetriedByMatching(accountList);
  }

  private void updateRetryState(BlindDate me, BlindDate you) {
    // 1. isRetry True 로 업데이트
    me.setIsRetry(true);

    // 2. 매칭 대상자 초기화
    me.setMatchingBlindDateId(null);
    you.setMatchingBlindDateId(null);

    // 3. 상대방 거절 업데이트
    you.setIsRejected(true);

    // 4. 상태 업데이트
    me.setGState(GBlindDateState.REMATCH);
    you.setGState(GBlindDateState.MATCH_REJECTED);
  }

  public Boolean getIsRetryBySeason(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    return blindDate.getIsRetry();
  }

  public Boolean getIsReportMannersBySeason(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    return StringUtils.isNotBlank(blindDate.getMannersReason());
  }

  @Transactional
  public void reportManners(Account account, Integer season, MannerReportRequestDTO request) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.setMannersReason(request.getReason());
    blindDate.setMannersReasonDesc(request.getReasonDesc());

    // todo: 상대방 비매너 신고 안내 (푸시 알림)
    // 1안) 신고내용을 관리자에게 알림 보낸다,  2안) 신고내용을 상대방에게 알림 보낸다
    mobilePushService.pushAdminBlindAccusation(accountQueryRepository.getAdminAccountList());
  }

  @Transactional
  public void updateBlindDateBaseMatching(Integer season) {
    blindDateMatchingHelperRepository.deleteAll();

    List<BlindDate> dateList = blindDateRepository.findAllBySeason(season).stream()
      .filter(it -> it.getMatchingBlindDateId() == null)
      .filter(BlindDate::getIsPayed)
      .collect(Collectors.toList());
    List<BlindDate> femaleList = dateList.stream()
      .filter(it -> Objects.equals(it.getGender(), "F"))
      .collect(Collectors.toList());
    List<BlindDate> maleList = dateList.stream()
      .filter(it -> Objects.equals(it.getGender(), "M"))
      .collect(Collectors.toList());

    // 필수 조건 매핑 체크
    for (BlindDate female : femaleList) {
      List<Long> tmpList = new ArrayList<>();

      for (BlindDate male : maleList) {
        if (checkAvailableMatching(female, male) && checkAvailableMatching(male, female)) {
          tmpList.add(male.getId());
        }
      }

      List<BlindDateMatchingHelper> matchingList = new ArrayList<>();
      for (Long id : tmpList) {
        BlindDateMatchingHelper match = BlindDateMatchingHelper.builder()
          .femaleId(female.getId())
          .maleId(id)
          .season(season)
          .build();
        matchingList.add(match);
      }
      blindDateMatchingHelperRepository.saveAll(matchingList);
    }
  }

  private boolean checkAvailableMatching(BlindDate base, BlindDate matched) {
    boolean goodMatching = true;

    if (Objects.nonNull(base.getFavoriteDrinkImportant()) && base.getFavoriteDrinkImportant()) {
      if (Objects.equals(base.getFavoriteDrink(), "NEVER") &&
        !Objects.equals(matched.getMyDrink(), "NEVER")) {
        goodMatching = false;
      }
    }

    if (Objects.nonNull(base.getFavoriteFaithImportant()) && base.getFavoriteFaithImportant()) {
      if (Objects.equals(base.getFavoriteFaith(), "CHRISTIAN") &&
        !Objects.equals(matched.getFaith(), "CHRISTIAN")) {
        goodMatching = false;
      }
    }

    if (Objects.nonNull(base.getFavoriteLocationImportant()) && base.getFavoriteLocationImportant()) {
      if (Objects.equals(base.getFavoriteLocation(), "LONG_NO") &&
        !Objects.equals(matched.getMyLocation(), base.getMyLocation())) {
        goodMatching = false;
      }
    }

    if (Objects.nonNull(base.getFavoriteSmokeImportant()) && base.getFavoriteSmokeImportant()) {
      if (! Objects.equals(base.getFavoriteSmoke(), matched.getSmoke())) {
        goodMatching = false;
      }
    }

    return goodMatching;
  }

  public HashMap<Long, List<Long>> getBlindDateBaseMatching(Integer season) {
    HashMap<Long, List<Long>> matchingMap = new HashMap<>();
    List<BlindDateMatchingHelper> matchingList = blindDateMatchingHelperRepository.findAllBySeason(season);

    Map<Long, List<BlindDateMatchingHelper>> map = matchingList.stream()
      .collect(Collectors.groupingBy(BlindDateMatchingHelper::getFemaleId));
    for (Long id : map.keySet()) {
      List<BlindDateMatchingHelper> match = map.get(id);
      matchingMap.put(id, match.stream().map(BlindDateMatchingHelper::getMaleId).collect(Collectors.toList()));
    }

    return matchingMap;
  }

  public BlindDateStepResponseDTO getSteps(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    return BlindDateStepResponseDTO.builder()
      .myStep(blindDate.getMyStep())
      .favoriteStep(blindDate.getFavoriteStep())
      .build();
  }

  public BlindDateResponseDTO getMyApply(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));
    BlindDateResponseDTO response = BlindDateResponseDTO.of(blindDate);

    List<String> images = blindDateImageLinkRepository.findAllByBlindDate(blindDate).stream()
      .map(BlindDateImageLink::getLink)
      .collect(Collectors.toList());
    response.setImageLinks(images);

    List<ExcludeCondResponseDTO> excludeCondList = excludeCondRepository.findAllByBlindDate(blindDate).stream()
      .map(ExcludeCondResponseDTO::of)
      .collect(Collectors.toList());
    response.setExcludeCondList(excludeCondList);

    return response;
  }

  @Transactional
  public void deleteBlindDateBySeason(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    // 1. exclude cond 리스트 삭제
    excludeCondRepository.deleteAllByBlindDate(blindDate);

    // 2. blind_date_image_link 삭제
    blindDateImageLinkRepository.deleteAllByBlindDate(blindDate);

    // 3. blind_date 삭제
    blindDateRepository.deleteById(blindDate.getId());
  }

  public GBlindDateState getGBlindDateState(Account account, Integer season) {

    Optional<BlindDate> optionalBlindDate = blindDateRepository.findByAccountAndSeason(account, season);

    // 참여하지 않은 경우
    if (optionalBlindDate.isEmpty()) {
      return GBlindDateState.NO;
    }

    // 참여한 경우
    BlindDate blindDate = optionalBlindDate.get();
    return blindDate.getGState();
  }

  @Transactional
  public void requestRecallFee(Account account, RecallRequestDTO request) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, request.getSeason())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.setIsActive(true);
    blindDate.setIsRecalled(request.getIsRecall());

    if (request.getIsRecall()) {                      // 환불 받고 시즌 종료
      blindDate.setGState(GBlindDateState.FINISHED);
    } else {                                          // 2차 매칭 시작
      blindDate.setGState(GBlindDateState.REMATCH);

      // 2차 매칭 오픈 예정 알림
      List<Account> accountList = Collections.singletonList(blindDate.getAccount());
      mobilePushService.pushRematch(accountList);
    }
  }

  @Transactional
  public void approveScreen(Long blindId) {
    BlindDate blindDate = blindDateRepository.findById(blindId)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.setIsScreened(true);
    blindDate.setGState(GBlindDateState.FEE_ING);

    // 내부 검수 완료 알림
    List<Account> accountList = Collections.singletonList(blindDate.getAccount());
    mobilePushService.pushAdminScreenResult(accountList);
  }

  @Transactional
  public void requestRematch(Account account, ReMatchRequestDTO request) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, request.getSeason())
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    if (request.getIsWantToRematch()) {              // 필수 조건 변경 후 재매칭 신청
      blindDate.updateBlindDateByRematch(request);
      blindDate.setIsRetry(true);
      blindDate.setGState(GBlindDateState.REMATCH);
    } else {                                         // 시즌 종료
      blindDate.setGState(GBlindDateState.FINISHED);
    }
  }

  @Transactional
  public void requestExtraInfo(Account account, GExtraInfo request, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    blindDate.setMyAge(request.getAge());
    blindDate.setKakaoLink(request.getKakaoLink());
    blindDate.setCommentForMate(request.getComment());
  }

  public GExtraInfo getExtraInfo(Account account, Integer season) {
    BlindDate blindDate = blindDateRepository.findByAccountAndSeason(account, season)
      .orElseThrow(() -> new BlindDateNotFoundException(BlindDateResponseCode.BLIND_DATE_READ_FAIL));

    return GExtraInfo.builder()
      .age(blindDate.getMyAge())
      .comment(blindDate.getCommentForMate())
      .kakaoLink(blindDate.getKakaoLink())
      .build();
  }

  public String getNoticeComment() {
    NoticeComment comment = noticeCommentRepository.findFirstByOrderByIdDesc()
            .orElseGet(() -> NoticeComment.builder().description("").build());
    return comment.getDescription();
  }

  public RealDataResponseDTO getRealData() {
    GenderData genderData = GenderData.builder()
            .male("여 20")
            .female("남 12")
            .build();

    FaithData faithData = FaithData.builder()
            .christian("기독교 20")
            .no("무교 20")
            .etc("그외 20")
            .build();

    LocationData locationData = LocationData.builder()
            .topList(List.of("서울", "경북", "포항"))
            .fullList(List.of("서울 / 22명", "충청도 / 22명", "경남 / 22명", "포항 / 22명", "포항 / 22명", "포항 / 22명", "포항 / 22명","포항 / 22명", "포항 / 22명"))
            .build();

    return RealDataResponseDTO.builder()
            .gender(genderData)
            .faith(faithData)
            .location(locationData)
            .build();
  }
}
