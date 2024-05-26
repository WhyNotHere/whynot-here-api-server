package handong.whynot.dto.blind_date;

import handong.whynot.domain.ExcludeCond;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlindDateRequestDTO {

  // =========== 메타 정보 ===========
  private Integer season;
  private Integer myStep;
  private Integer favoriteStep;

  // =========== 내 정보 ===========
  private String name;
  private String gender;
  private Integer myAge;      // 졸업생 시즌1 까지 사용
  private String myAgeStr;    // 그 이후 현재 사용 (ex. 00년생)
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
  private String mbti;
  private String smoke;
  private String commentForMate;
  private String commentForAdmin;
  private String kakaoLink;
  private List<ExcludeCond> excludeCondList;
  private List<String> imageLinks;
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
  private String favoriteLocationDesc;
}
