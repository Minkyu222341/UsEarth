package sparta.seed.mission.domain.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import sparta.seed.mission.domain.ClearMission;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ClearMissionResponseDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate createdAt;
	private long count;
	private List<ClearMission> clearMissionList;

	@Builder
	public ClearMissionResponseDto(LocalDate selectedDate, List<ClearMission> clearMissionList, int clearMissionCnt) {
		this.createdAt = selectedDate;
		this.count = clearMissionCnt;
		this.clearMissionList = clearMissionList;
	}
	@QueryProjection
	public ClearMissionResponseDto(LocalDate createdAt ,long count) {
		this.createdAt = createdAt;
		this.count = count;
	}
}
