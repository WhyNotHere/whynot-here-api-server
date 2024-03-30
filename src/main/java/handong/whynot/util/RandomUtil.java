package handong.whynot.util;

import java.util.Random;

public class RandomUtil {
  public static String generateRandomNumber(int length) {
    Random random = new Random();

    // 지정된 길이 만큼의 범위에서 난수 생성
    int upperBound = (int) Math.pow(10, length);
    int randomNumber = random.nextInt(upperBound);

    // 지정된 길이로 맞추기 위해 문자열로 변환하고, 앞에 0을 채워넣음
    return String.format("%0" + length + "d", randomNumber);
  }
}
