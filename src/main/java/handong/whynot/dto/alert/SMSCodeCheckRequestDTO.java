package handong.whynot.dto.alert;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSCodeCheckRequestDTO {
  private String smsCode;
}
