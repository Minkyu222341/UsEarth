package sparta.seed.community.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //닉네임
  private String nickname;
  //작성한 유저의 Pk
  private Long memberId;
  //내용
  private String content;
  private String img;
  //인증글
  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "proof_id")
  private Proof proof;

  @Builder
  public Comment(String nickname, Long memberId, String content, String img, Proof proof) {
    this.nickname = nickname;
    this.memberId = memberId;
    this.content = content;
    this.img = img;
    this.proof = proof;
  }

  public void update(String content,String nickname){
    this.content = content;
    this.nickname = nickname;
  }

  public void setImg(String img) {
    this.img = img;
  }
}
