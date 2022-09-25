package sparta.seed.mission.repository.customrepository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sparta.seed.mission.domain.dto.requestdto.MissionSearchCondition;
import sparta.seed.mission.domain.dto.responsedto.ClearMissionResponseDto;
import sparta.seed.mission.domain.dto.responsedto.QClearMissionResponseDto;
import sparta.seed.mission.repository.customrepository.ClearMissionRepositoryCustom;
import sparta.seed.util.DateUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static sparta.seed.mission.domain.QClearMission.clearMission;


@RequiredArgsConstructor
public class ClearMissionRepositoryImpl implements ClearMissionRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final DateUtil dateUtil;

  @Override
  public List<ClearMissionResponseDto> dailyMissionStats(MissionSearchCondition condition, Long memberId) {
    List<LocalDate> dateScope = dateUtil.scopeOfStats(condition);
    List<ClearMissionResponseDto> result = queryFactory.select(new QClearMissionResponseDto(clearMission.clearTime, clearMission.count()))
            .from(clearMission)
            .where(clearMission.clearTime.between(dateScope.get(0), dateScope.get(1)), clearMission.memberId.eq(memberId))
            .groupBy(clearMission.clearTime)
            .fetch();
    return result;
  }

  @Override
  public List<Long> WeekMissionStats(MissionSearchCondition condition, Long memberId) {
    return null;
  }
}
