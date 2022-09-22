package sparta.seed.mission.domain.dto.responsedto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sparta.seed.mission.domain.ClearMission;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ClearMissionResponseDto {
	private LocalDate clearTime;
	private long count;

	private List<ClearMission> clearMissionList;

	@Builder
	public ClearMissionResponseDto(LocalDate clearTime, List<ClearMission> clearMissionList, int count) {
		this.clearTime = clearTime;
		this.count = count;
		this.clearMissionList = clearMissionList;
	}
	@QueryProjection
	public ClearMissionResponseDto(LocalDate clearTime ,long count) {
		this.clearTime = clearTime;
		this.count = count;
	}
}
