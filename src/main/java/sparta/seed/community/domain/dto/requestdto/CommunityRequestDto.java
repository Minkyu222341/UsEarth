package sparta.seed.community.domain.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CommunityRequestDto {
  @NotEmpty(message = "시작 날짜를 입력해 주세요.")
  private String startDate;
  @NotEmpty(message = "종료 날짜를 입력해 주세요.")
  private String endDate;
  @NotNull(message = "달성목표를 설정해 주세요.")
  private Integer limitScore;
  @NotNull(message = "참가인원을 설정해 주세요.")
  private Integer limitParticipants;
  private boolean secret;
  private String password;
  @NotEmpty(message = "제목을 입력해 주세요.")
  private String title;
  @NotEmpty(message = "내용을 입력해 주세요.")
  private String content;
  private boolean delete;
  private String changeNickname;
}
