package sparta.seed.campaign.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class AqApiData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String category;
	private String region;
	private Double amount;
	private String datetime;

	@Builder
	public AqApiData(String category, String region, Double amount, String datetime) {
		this.category = category;
		this.region = region;
		this.amount = amount;
		this.datetime = datetime;
	}
}


