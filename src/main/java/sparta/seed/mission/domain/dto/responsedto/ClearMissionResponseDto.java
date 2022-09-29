package sparta.seed.mission.domain.dto.responsedto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sparta.seed.mission.domain.ClearMission;

import java.util.List;

@Getter
public class ClearMissionResponseDto {
	private String clearTime;
	private long count;

	private List<ClearMission> clearMissionList;

	@Builder
	public ClearMissionResponseDto(String clearTime, List<ClearMission> clearMissionList, int count) {
		this.clearTime = clearTime;
		this.count = count;
		this.clearMissionList = clearMissionList;
	}
	@QueryProjection
	public ClearMissionResponseDto(String clearTime ,long count) {
		this.clearTime = clearTime;
		this.count = count;
	}
}
