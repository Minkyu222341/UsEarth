package sparta.seed.campaign.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.seed.util.Timestamped;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class Campaign extends Timestamped {
  //PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String thumbnail;
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
