package sparta.seed.community.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import sparta.seed.community.domain.dto.requestdto.ProofRequestDto;
import sparta.seed.img.domain.Img;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Proof extends BaseEntity {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String nickname;
  @Column(nullable = false)
  private Long memberId;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String content;


  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "community_id",nullable = false)
  private Community community;

  //이미지리스트
  @OneToMany(mappedBy = "proof", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<Img> imgList = new ArrayList<>();

  //댓글리스트
  @OneToMany(mappedBy = "proof",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  private List<Comment> commentList = new ArrayList<>();

  //좋아요
  @OneToMany(mappedBy = "proof", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Heart> heartList = new ArrayList<>();

  @Builder
  public Proof(Long memberId, String nickname, String title, String content, Community community) {
    this.memberId = memberId;
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.community = community;
  }

  public void updateProof(String title, String content, String nickname) {
    this.title = title;
    this.content = content;
    this.nickname = nickname;
  }

  public void addImg(Img img){
    this.imgList.add(img);
  }
  public void addComment(Comment comment){
    this.commentList.add(comment);
  }
  public void removeComment(Comment comment){
    this.commentList.remove(comment);
  }

  public void addHeart(Heart heart){
    this.heartList.add(heart);
  }
  public void removeHeart(Heart heart){
    this.heartList.remove(heart);
  }


}
