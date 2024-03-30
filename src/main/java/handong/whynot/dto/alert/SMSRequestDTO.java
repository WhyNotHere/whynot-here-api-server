package handong.whynot.dto.alert;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequestDTO {
  private String phoneNumber;
  private String message;
}
