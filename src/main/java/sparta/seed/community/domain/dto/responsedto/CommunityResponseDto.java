package sparta.seed.community.domain.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sparta.seed.community.domain.Participants;

import java.util.List;

@Getter
@Setter
public class CommunityResponseDto {

  private Long communityId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private String createAt;
  private String nickname;
  private String title;
  private String img;
  //모임 참가자
  private List<Participants> participantsList;
  private String content;
  private Integer participantsCnt;
  private int limitParticipants;
  //참여 퍼센트
  private int currentPercent;
  //로그인한 사용자가 현재 모임에 참여했는지 여부
  private boolean participant;
  //목표 인증글 수
  private int limitScore;
  //인증 퍼센트
  private int successPercent;
  private long currentCertifiedProof;
  private String startDate;
  private String endDate;
  //진행 여부
  private String dateStatus;
  private boolean secret;
  private String password;
  private boolean writer;

  @QueryProjection
  @Builder
  public CommunityResponseDto(Long communityId, String img, String title, boolean participant, List<Participants> participantsList, double limitScore, double limitParticipants, double successPercent, long currentCertifiedProof, double currentPercent, Integer participantsCnt, String nickname, String startDate, String endDate, boolean secret, String password, String content, String dateStatus, String createAt, boolean writer) {
    this.communityId = communityId;
    this.createAt = createAt;
    this.nickname = nickname;
    this.title = title;
    this.content = content;
    this.img = img;
    this.participantsList = participantsList;
    this.participantsCnt = participantsCnt;
    this.limitParticipants = (int)limitParticipants;
    this.currentPercent = (int) currentPercent;
    this.participant = participant;
    this.limitScore = (int)limitScore;
    this.successPercent = (int) successPercent;
    this.currentCertifiedProof = currentCertifiedProof;
    this.startDate = startDate;
    this.endDate = endDate;
    this.dateStatus = dateStatus;
    this.secret = secret;
    this.password = password;
    this.writer = writer;
  }
}
