package sparta.seed.campaign.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Campaign extends BaseEntity {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String thumbnail;
  @Column(nullable = false)
  //제목
  private String title;

  private String thumbnailUrl;

  @Builder
  public Campaign(Long id, String thumbnail, String title, String thumbnailUrl) {
    this.id = id;
    this.thumbnail = thumbnail;
    this.title = title;
    this.thumbnailUrl = thumbnailUrl;
  }



  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

}
