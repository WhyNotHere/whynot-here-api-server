package handong.whynot.dto.admin;

import handong.whynot.domain.BlindDate;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminBlindDateResponseDTO {

  // =========== 메타 정보 ===========
  private Long blindDateId;
  private Boolean matchedByAdmin;
  private Boolean isSubmitted;
  private Boolean isRetry;

  // =========== 내 정보 ===========
  private String name;
  private String gender;
  private Integer myAge;
  private Integer myHeight;
  private String myDrink;
  private String myLocation;
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
  private String retryReason;
  private String mannersReason;
  private String mannersReasonDesc;
  private String department;
  private String myJob;
  private String myJobDesc;

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

  public static AdminBlindDateResponseDTO of(BlindDate blindDate) {
    return builder()
      // =========== 메타 정보 ===========
      .blindDateId(blindDate.getId())
      .matchedByAdmin(blindDate.getMatchingBlindDateId() != null)
      .isSubmitted(blindDate.getIsSubmitted())
      .isRetry(blindDate.getIsRetry())
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
      .mbti(blindDate.getMbti())
      .smoke(blindDate.getSmoke())
      .commentForMate(blindDate.getCommentForMate())
      .commentForAdmin(blindDate.getCommentForAdmin())
      .kakaoLink(blindDate.getKakaoLink())
      .retryReason(blindDate.getRetryReason())
      .mannersReason(blindDate.getMannersReason())
      .mannersReasonDesc(blindDate.getMannersReasonDesc())
      .department(blindDate.getDepartment())
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
