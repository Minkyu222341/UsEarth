package sparta.seed.mission.domain.dto.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionDetailResponseDto {
	private String missionName;
	private String difficulty;
	private boolean complete;

	public MissionDetailResponseDto(String missionName, String difficulty, boolean complete) {
		this.missionName = missionName;
		this.difficulty = difficulty;
		this.complete = complete;
	}
}
