package sparta.seed.community.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.boot.context.properties.bind.DefaultValue;
import sparta.seed.community.domain.dto.requestdto.CommunityRequestDto;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Community extends BaseEntity {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //제목
  private String title;
  @Column(nullable = false)
  //작성자
  private String nickname;
  @Column(nullable = false)
  //작성한 유저의 PK
  private Long memberId;
  //내용
  @Column(nullable = false)
  private String content;
  private String img;
  //캠페인시작일
  @Column(nullable = false)
  private String startDate;
  //캠페인마감일
  @Column(nullable = false)
  private String endDate;
  //목표 달성 횟수
  @Column(nullable = false)
  private double limitScore;
  //참가인원 제한
  @Column(nullable = false)
  private double limitParticipants;
  @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Proof> proofList = new ArrayList<>();

  @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Participants> participantsList = new ArrayList<>();



  @Builder
  public Community(Long id, String title, String nickname, Long memberId, String content, String img, String startDate, String endDate, double limitScore, double limitParticipants, List<Proof> proofList, List<Participants> participantsList) {
    this.id = id;
    this.title = title;
    this.nickname = nickname;
    this.memberId = memberId;
    this.content = content;
    this.img = img;
    this.startDate = startDate;
    this.endDate = endDate;
    this.limitScore = limitScore;
    this.limitParticipants = limitParticipants;
    this.proofList = proofList;
    this.participantsList = participantsList;
  }

  public void update(CommunityRequestDto requestDto,String nickname) {
    this.startDate = requestDto.getStartDate();
    this.endDate = requestDto.getEndDate();
    this.limitScore = requestDto.getLimitScore();
    this.limitParticipants = requestDto.getLimitParticipants();
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.nickname = nickname;
  }

  public void addParticipant(Participants participants) {
    participantsList.add(participants);
  }

  public void setImg(String img) {
    this.img = img;
  }


}