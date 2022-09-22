package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantResponseDto {
  private Long id;
  private String nickname;

  @Builder
  public ParticipantResponseDto(Long id, String nickname) {
    this.id = id;
    this.nickname = nickname;
  }
}
