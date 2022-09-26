package sparta.seed.campaign.domain.dto.requestdto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

import static sparta.seed.msg.ResponseMsg.LOGOUT_SUCCESS;

@Getter
public class CampaignRequestDto {
	private String title;

}
