package sparta.seed.campaign.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class AqApiData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String category;
	@Column(nullable = false)
	private String region;
	@Column(nullable = false)
	private double amount;
	@Column(nullable = false)
	private String datetime;

	@Builder
	public AqApiData(String category, String region, double amount, String datetime) {
		this.category = category;
		this.region = region;
		this.amount = amount;
		this.datetime = datetime;
	}
}


