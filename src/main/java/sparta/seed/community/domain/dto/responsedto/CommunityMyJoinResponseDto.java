package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;

public class CommunityMyJoinResponseDto {
	private Long communityId;
	private String title;
	private String img;
	private double currentPercent;
	private double successPercent;
	private String startDate;
	private String endDate;
	private String dateStatus;

	@Builder
	public CommunityMyJoinResponseDto(Long communityId, String title, String img, double currentPercent, double successPercent, String startDate, String endDate, String dateStatus) {
		this.communityId = communityId;
		this.title = title;
		this.img = img;
		this.currentPercent = currentPercent;
		this.successPercent = successPercent;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateStatus = dateStatus;
	}
}