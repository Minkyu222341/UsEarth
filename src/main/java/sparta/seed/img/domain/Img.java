package sparta.seed.img.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.seed.community.domain.Proof;
import sparta.seed.util.BaseEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Img extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String imgUrl;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JsonBackReference
  @JoinColumn(name = "proofId")
  private Proof proof;

  @Builder
  public Img(Long id, String imgUrl, Proof proof) {
    this.id = id;
    this.imgUrl = imgUrl;
    this.proof = proof;
  }
}
