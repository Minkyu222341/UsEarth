package sparta.seed.login.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponseDto {
  private String accessToken;
  private String refreshToken;
  private Long memberId;

  @Builder
  public TokenResponseDto(String accessToken, String refreshToken,Long memberId) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.memberId = memberId;
  }
}
