package handong.whynot.dto.blind_date;

import handong.whynot.domain.BlindDate;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlindDateResponseDTO {

  // =========== 메타 정보 ===========
  private Integer myStep;
  private Integer favoriteStep;

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
  private String myJob;
  private String myJobDesc;
  private String hobby;
  private String hobbyDesc;
  private String myContactStyle;
  private String myCharacter;
  private String dateStyle;
  private String faith;
  private String mbti;
  private String smoke;
  private String commentForMate;
  private String commentForAdmin;
  private String kakaoLink;
  private String department;
  private List<String> imageLinks;
  private List<ExcludeCondResponseDTO> excludeCondList;
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
  private String favoriteLocationDesc;

  public static BlindDateResponseDTO of(BlindDate blindDate) {
    return builder()
      // =========== 메타 정보 ===========
      .myStep(blindDate.getMyStep())
      .favoriteStep(blindDate.getFavoriteStep())
      // =========== 내 정보 ===========
      .name(blindDate.getName())
      .gender(blindDate.getGender())
      .myAge(blindDate.getMyAge())
            .myAgeStr(blindDate.getMyAgeStr())
      .myHeight(blindDate.getMyHeight())
      .myDrink(blindDate.getMyDrink())
      .myLocation(blindDate.getMyLocation())
            .myLocationSub(blindDate.getMyLocationSub())
      .myLocationDesc(blindDate.getMyLocationDesc())
      .myJob(blindDate.getMyJob())
      .myJobDesc(blindDate.getMyJobDesc())
      .hobby(blindDate.getHobby())
      .hobbyDesc(blindDate.getHobbyDesc())
      .myContactStyle(blindDate.getMyContactStyle())
      .myCharacter(blindDate.getMyCharacter())
      .dateStyle(blindDate.getDateStyle())
      .faith(blindDate.getFaith())
      .mbti(blindDate.getMbti())
      .smoke(blindDate.getSmoke())
      .commentForMate(blindDate.getCommentForMate())
      .commentForAdmin(blindDate.getCommentForAdmin())
      .kakaoLink(blindDate.getKakaoLink())
      .department(blindDate.getDepartment())
            .holidayType(blindDate.getHolidayType())
            .conflictSolving(blindDate.getConflictSolving())
            .myDreamGoal(blindDate.getMyDreamGoal())
            .myPreference(blindDate.getMyPreference())
            .loveView(blindDate.getLoveView())
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
            .favoriteLocationDesc(blindDate.getFavoriteLocationDesc())
      .build();
  }
}
