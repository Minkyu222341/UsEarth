package sparta.seed.member.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;
import sparta.seed.member.domain.LoginType;

@Getter
public class UserInfoResponseDto {
	private Long id;
	private String nickname;
	private String username;
	private String profileImage;
	private int level;
	private int totalClear;
	private int nextLevelExp;
	private int needNextLevelExp;
	private boolean isSecret;
	private LoginType loginType;


	@Builder
	public UserInfoResponseDto(Long id, String nickname, String username, String profileImage, int level, int totalClear, int nextLevelExp, int needNextLevelExp, boolean isSecret,LoginType loginType) {
		this.id = id;
		this.nickname = nickname;
		this.username = username;
		this.profileImage = profileImage;
		this.level = level;
		this.totalClear = totalClear;
		this.nextLevelExp = nextLevelExp;
		this.needNextLevelExp = needNextLevelExp;
		this.isSecret = isSecret;
		this.loginType = loginType;
	}
}