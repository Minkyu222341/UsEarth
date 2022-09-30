package sparta.seed.member.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sparta.seed.member.domain.dto.requestdto.NicknameRequestDto;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String nickname;
  @Column(nullable = false)
  private String socialId;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Authority authority;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoginType loginType;
  @Column(nullable = false)
  private String profileImage;
  private boolean isSecret;
  private int exp;
  private int level;
  @ElementCollection
  private Map<String,Boolean> dailyMission = new HashMap<>(6,1);



  @Builder
  public Member(Long id, String username, String password, String nickname, String socialId, Authority authority, String profileImage,int exp,int level,LoginType loginType) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.socialId = socialId;
    this.authority = authority;
    this.profileImage = profileImage;
    this.exp = exp;
    this.level = level;
    this.loginType = loginType;
  }

  public void updateNickname(NicknameRequestDto requestDto) {
    nickname = requestDto.getNickname();
  }

  public void updateIsSecret(boolean isSecret) {
    this.isSecret = isSecret;
  }

  public void addExp(int exp) {
    this.exp += exp;
  }

  public void initExp() {
    this.exp = 0;
  }

  public void minusExp(int exp) {
    this.exp -= exp;
  }
  public void levelUp() {
    this.level += 1;
  }
}