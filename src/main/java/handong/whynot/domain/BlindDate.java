package handong.whynot.domain;

import handong.whynot.domain.common.BaseTimeEntity;
import handong.whynot.dto.blind_date.BlindDateRequestDTO;
import handong.whynot.dto.blind_date.ReMatchRequestDTO;
import handong.whynot.dto.blind_date.enums.GBlindDateState;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BlindDate extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  // =========== 메타 정보 ===========
  @Column(name = "matching_blind_date_id")
  private Long matchingBlindDateId;

  @Column(name = "season")
  private Integer season;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "is_matched")
  private Boolean isMatched;

  @Column(name = "is_reveal")
  private Boolean isReveal;

  @Column(name = "is_reveal_2")
  private Boolean isReveal2;

  @Column(name = "is_submitted")
  private Boolean isSubmitted = false;

  @Column(name = "is_screened")
  private Boolean isScreened;

  @Column(name = "is_payed")
  private Boolean isPayed;

  @Column(name = "is_rejected")
  private Boolean isRejected;

  @Column(name = "is_recalled")
  private Boolean isRecalled;

  @Column(name = "is_retry")
  private Boolean isRetry;

  @Column(name = "g_state")
  @Enumerated(EnumType.STRING)
  private GBlindDateState gState;

  @Column(name = "my_step")
  private Integer myStep;

  @Column(name = "favorite_step")
  private Integer favoriteStep;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id")
  private Account account;

  // =========== 내 정보 ===========
  @Column(name = "name")
  private String name;

  @Column(name = "gender")
  private String gender;

  @Column(name = "my_age")
  private Integer myAge;

  @Column(name = "my_age_str")
  private String myAgeStr;

  @Column(name = "my_height")
  private Integer myHeight;

  @Column(name = "my_drink")
  private String myDrink;

  @Column(name = "my_location")
  private String myLocation;

  @Column(name = "my_location_sub")
  private String myLocationSub;

  @Column(name = "my_location_desc")
  private String myLocationDesc;

  @Column(name = "hobby")
  private String hobby;

  @Column(name = "hobby_desc")
  private String hobbyDesc;

  @Column(name = "my_contact_style")
  private String myContactStyle;

  @Column(name = "my_character")
  private String myCharacter;

  @Column(name = "date_style")
  private String dateStyle;

  @Column(name = "faith")
  private String faith;

  @Column(name = "mbti")
  private String mbti;

  @Column(name = "smoke")
  private String smoke;

  @Column(name = "comment_for_mate")
  private String commentForMate;

  @Column(name = "comment_for_admin")
  private String commentForAdmin;

  @Column(name = "kakao_link")
  private String kakaoLink;

  @Column(name = "retry_reason")
  private String retryReason;

  @Column(name = "manners_reason")
  private String mannersReason;

  @Column(name = "manners_reason_desc")
  private String mannersReasonDesc;

  @Column(name = "department")
  private String department;

  @Column(name = "my_job")
  private String myJob;

  @Column(name = "my_job_desc")
  private String myJobDesc;

  @Column(name = "holiday_type")
  private String holidayType;

  @Column(name = "conflict_solving")
  private String conflictSolving;

  @Column(name = "my_dream_goal")
  private String myDreamGoal;

  @Column(name = "my_preference")
  private String myPreference;

  @Column(name = "love_view")
  private String loveView;

  // =========== 선호하는 상대방 정보 ===========
  @Column(name = "favorite_age")
  private String favoriteAge;

  @Column(name = "favorite_age_important")
  private Boolean favoriteAgeImportant;

  @Column(name = "favorite_height")
  private String favoriteHeight;

  @Column(name = "favorite_height_important")
  private Boolean favoriteHeightImportant;

  @Column(name = "favorite_smoke")
  private String favoriteSmoke;

  @Column(name = "favorite_smoke_important")
  private Boolean favoriteSmokeImportant;

  @Column(name = "favorite_drink")
  private String favoriteDrink;

  @Column(name = "favorite_drink_important")
  private Boolean favoriteDrinkImportant;

  @Column(name = "favorite_faith")
  private String favoriteFaith;

  @Column(name = "favorite_faith_important")
  private Boolean favoriteFaithImportant;

  @Column(name = "favorite_location")
  private String favoriteLocation;

  @Column(name = "favorite_location_important")
  private Boolean favoriteLocationImportant;

  @Column(name = "favorite_location_desc")
  private String favoriteLocationDesc;

  public static BlindDate of(Integer season, Account account) {
    return builder()
      // =========== 메타 정보 ===========
      .matchingBlindDateId(null)
      .isActive(false)
      .isMatched(false)
      .season(season)
      .isReveal(false)
      .isSubmitted(false)
      .isScreened(false)
      .isPayed(false)
      .isRejected(false)
      .isRecalled(false)
      .isReveal2(false)
      .isRetry(false)
      .gState(GBlindDateState.BLIND_ING)
      .account(account)
      .myStep(0)
      .favoriteStep(0)
      .build();
  }

  public void updateMatchingBlindDate(Long matchingId) {
    matchingBlindDateId = matchingId;
  }

  public void updateBlindDate(BlindDateRequestDTO request) {
    // =========== 메타 정보 ===========
    this.myStep = request.getMyStep();
    this.favoriteStep = request.getFavoriteStep();

    // =========== 내 정보 ===========
    this.name = request.getName();
    this.gender = request.getGender();
    this.myAge = request.getMyAge();
    this.myAgeStr = request.getMyAgeStr();
    this.myHeight = request.getMyHeight();
    this.myDrink = request.getMyDrink();
    this.myLocation = request.getMyLocation();
    this.myLocationSub = request.getMyLocationSub();
    this.myLocationDesc = request.getMyLocationDesc();
    this.hobby = request.getHobby();
    this.hobbyDesc = request.getHobbyDesc();
    this.myContactStyle = request.getMyContactStyle();
    this.myCharacter = request.getMyCharacter();
    this.dateStyle = request.getDateStyle();
    this.faith = request.getFaith();
    this.mbti = request.getMbti();
    this.smoke = request.getSmoke();
    this.commentForMate = request.getCommentForMate();
    this.commentForAdmin = request.getCommentForAdmin();
    this.kakaoLink = request.getKakaoLink();
    this.department = request.getDepartment();
    this.myJob = request.getMyJob();
    this.myJobDesc = request.getMyJobDesc();
    this.holidayType = request.getHolidayType();
    this.conflictSolving = request.getConflictSolving();
    this.myDreamGoal = request.getMyDreamGoal();
    this.myPreference = request.getMyPreference();
    this.loveView = request.getLoveView();

    // =========== 선호하는 상대방 정보 ===========
    this.favoriteAge = request.getFavoriteAge();
    this.favoriteAgeImportant = request.getFavoriteAgeImportant();
    this.favoriteHeight = request.getFavoriteHeight();
    this.favoriteHeightImportant = request.getFavoriteHeightImportant();
    this.favoriteSmoke = request.getFavoriteSmoke();
    this.favoriteSmokeImportant = request.getFavoriteSmokeImportant();
    this.favoriteDrink = request.getFavoriteDrink();
    this.favoriteDrinkImportant = request.getFavoriteDrinkImportant();
    this.favoriteFaith = request.getFavoriteFaith();
    this.favoriteFaithImportant = request.getFavoriteFaithImportant();
    this.favoriteLocation = request.getFavoriteLocation();
    this.favoriteLocationImportant = request.getFavoriteLocationImportant();
    this.favoriteLocationDesc = request.getFavoriteLocationDesc();
  }

  public void updateBlindDateByRematch(ReMatchRequestDTO request) {
    this.favoriteAge = request.getFavoriteAge();
    this.favoriteAgeImportant = request.getFavoriteAgeImportant();
    this.favoriteHeight = request.getFavoriteHeight();
    this.favoriteHeightImportant = request.getFavoriteHeightImportant();
    this.favoriteSmoke = request.getFavoriteSmoke();
    this.favoriteSmokeImportant = request.getFavoriteSmokeImportant();
    this.favoriteDrink = request.getFavoriteDrink();
    this.favoriteDrinkImportant = request.getFavoriteDrinkImportant();
    this.favoriteFaith = request.getFavoriteFaith();
    this.favoriteFaithImportant = request.getFavoriteFaithImportant();
    this.favoriteLocation = request.getFavoriteLocation();
    this.favoriteLocationImportant = request.getFavoriteLocationImportant();
  }
}
