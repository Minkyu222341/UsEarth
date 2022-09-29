package sparta.seed.campaign.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AqApiResponseDto {
	private String category;
	private String amount;
	private String MaxAmount;
	private String risk;

	@Builder
	public AqApiResponseDto(String category, String amount) {
		this.category = category;
		this.amount = amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setMaxAmount(String maxAmount) {
		MaxAmount = maxAmount;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
}
