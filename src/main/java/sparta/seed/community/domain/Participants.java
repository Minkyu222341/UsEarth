package sparta.seed.community.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Participants {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Community community;
  @Column(nullable = false)
  private Long memberId;
  @Column(nullable = false)
  private String nickname;

  @Builder
  public Participants(Long id,Community community, Long memberId,String nickname) {
    this.id = id;
    this.community = community;
    this.memberId = memberId;
    this.nickname = nickname;
  }
}
