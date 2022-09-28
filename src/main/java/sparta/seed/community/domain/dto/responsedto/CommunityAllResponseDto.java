package sparta.seed.community.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityAllResponseDto {
	private Long communityId;
	private String nickname;
	private String title;
	private String img;
	private long currentPercent;
	private long successPercent;
	private String dateStatus;
	private boolean secret;
	private String password;
	private boolean writer;

	@Builder
	public CommunityAllResponseDto(Long communityId, String nickname, String title, String img, double currentPercent, double successPercent, String dateStatus, boolean secret, String password, boolean writer) {
		this.communityId = communityId;
		this.nickname = nickname;
		this.title = title;
		this.img = img;
		this.currentPercent = (long) currentPercent;
		this.successPercent = (long) successPercent;
		this.dateStatus = dateStatus;
		this.secret = secret;
		this.password = password;
		this.writer = writer;
	}
}