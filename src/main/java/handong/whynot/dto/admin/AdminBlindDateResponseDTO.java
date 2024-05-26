package handong.whynot.dto.admin;

import handong.whynot.domain.BlindDate;
import handong.whynot.dto.blind_date.enums.GBlindDateState;
import lombok.*;

import java.util.List;

import static handong.whynot.dto.blind_date.enums.DepartmentEnum.getDepartmentEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminBlindDateResponseDTO {

  // =========== 메타 정보 ===========
  private Long blindDateId;
  private Boolean matchedByAdmin;
  private GBlindDateState gState;

  // =========== 내 정보 ===========
  private String name;
  private String gender;
  private Integer myAge;
  private String myAgeStr;
  private Integer myHeight;
  private String myDrink;
  private String myLocation;
  private String myLocationSub;
  private String myLocationDesc;
  private String hobby;
  private String hobbyDesc;
  private String myContactStyle;
  private String myCharacter;
  private String dateStyle;
  private String faith;
  private String smoke;
  private String commentForMate;
  private String commentForAdmin;
  private String kakaoLink;
  private String retryReason;
  private String mannersReason;
  private String mannersReasonDesc;
  private String department;
  private String myJob;
  private String myJobDesc;
  private String holidayType;
  private String conflictSolving;
  private String myDreamGoal;
  private String myPreference;
  private String loveView;

  // =========== 선호하는 상대방 정보 ===========
  private String favoriteAge;
  private Boolean favoriteAgeImportant;
  private String favoriteHeight;
  private Boolean favoriteHeightImportant;
  private String favoriteSmoke;
  private Boolean favoriteSmokeImportant;
  private String favoriteDrink;
  private Boolean favoriteDrinkImportant;
  private String favoriteFaith;
  private Boolean favoriteFaithImportant;
  private String favoriteLocation;
  private Boolean favoriteLocationImportant;
  private List<Long> baseMatching;
  private String favoriteLocationDesc;

  public static AdminBlindDateResponseDTO of(BlindDate blindDate) {
    return builder()
      // =========== 메타 정보 ===========
      .blindDateId(blindDate.getId())
      .matchedByAdmin(blindDate.getMatchingBlindDateId() != null)
      .gState(blindDate.getGState())
      // =========== 내 정보 ===========
      .name(blindDate.getName())
      .gender(blindDate.getGender())
      .myAge(blindDate.getMyAge())
      .myHeight(blindDate.getMyHeight())
      .myDrink(blindDate.getMyDrink())
      .myLocation(blindDate.getMyLocation())
      .myLocationDesc(blindDate.getMyLocationDesc())
      .hobby(blindDate.getHobby())
      .hobbyDesc(blindDate.getHobbyDesc())
      .myContactStyle(blindDate.getMyContactStyle())
      .myCharacter(blindDate.getMyCharacter())
      .dateStyle(blindDate.getDateStyle())
      .faith(blindDate.getFaith())
      .smoke(blindDate.getSmoke())
      .commentForMate(blindDate.getCommentForMate())
      .commentForAdmin(blindDate.getCommentForAdmin())
      .kakaoLink(blindDate.getKakaoLink())
      .retryReason(blindDate.getRetryReason())
      .mannersReason(blindDate.getMannersReason())
      .mannersReasonDesc(blindDate.getMannersReasonDesc())
      .department(getDepartmentEnum(blindDate.getDepartment()).getDesc())
      .myJob(blindDate.getMyJob())
      .myJobDesc(blindDate.getMyJobDesc())
      // =========== 선호하는 상대방 정보 ===========
      .favoriteAge(blindDate.getFavoriteAge())
      .favoriteAgeImportant(blindDate.getFavoriteAgeImportant())
      .favoriteHeight(blindDate.getFavoriteHeight())
      .favoriteHeightImportant(blindDate.getFavoriteHeightImportant())
      .favoriteSmoke(blindDate.getFavoriteSmoke())
      .favoriteSmokeImportant(blindDate.getFavoriteSmokeImportant())
      .favoriteDrink(blindDate.getFavoriteDrink())
      .favoriteDrinkImportant(blindDate.getFavoriteDrinkImportant())
      .favoriteFaith(blindDate.getFavoriteFaith())
      .favoriteFaithImportant(blindDate.getFavoriteFaithImportant())
      .favoriteLocation(blindDate.getFavoriteLocation())
      .favoriteLocationImportant(blindDate.getFavoriteLocationImportant())
      .build();
  }
}
