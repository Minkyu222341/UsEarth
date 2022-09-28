package sparta.seed.mission.domain.dto.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionClearResponseDto {
	private String missionName;
	private boolean complete;
	private int level;
	private int totalClear;
	private int nextLevelExp;
	private int needNextLevelExp;

	@Builder
	public MissionClearResponseDto(String missionName, boolean complete, int level, int totalClear, int nextLevelExp,int needNextLevelExp) {
		this.missionName = missionName;
		this.complete = complete;
		this.level = level;
		this.totalClear = totalClear;
		this.nextLevelExp = nextLevelExp;
		this.needNextLevelExp =needNextLevelExp;
	}
}
