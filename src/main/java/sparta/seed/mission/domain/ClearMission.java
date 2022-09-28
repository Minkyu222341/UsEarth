package sparta.seed.mission.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
@Entity
public class ClearMission{
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //완료한 유저의 Pk
  private Long memberId;
  //완료한 미션
  private String content;
  private LocalDate clearTime;

  @Builder
  public ClearMission(Long memberId, String content, LocalDate clearTime) {
    this.memberId = memberId;
    this.content = content;
    this.clearTime = clearTime;
  }
}
