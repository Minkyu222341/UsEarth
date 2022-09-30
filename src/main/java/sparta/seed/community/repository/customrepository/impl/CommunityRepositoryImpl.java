package sparta.seed.community.repository.customrepository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import sparta.seed.community.domain.Community;
import sparta.seed.community.domain.dto.requestdto.CommunitySearchCondition;
import sparta.seed.community.repository.customrepository.CommunityRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static sparta.seed.community.domain.QCommunity.community;


@RequiredArgsConstructor
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public QueryResults<Community> getAllCommunity(Pageable pageable, CommunitySearchCondition condition) {
    return queryFactory // querydsl 강의 뒷쪽 페이징 참고
            .selectFrom(community)
            .where(titleEq(condition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(community.id.desc())
            .where()
            .fetchResults();
  }

  public List<Community> activeCommunity() {
    return queryFactory.selectFrom(community)
            .where(community.endDate.goe(String.valueOf(LocalDate.now())), community.startDate.loe(String.valueOf(LocalDate.now())), (community.proofList.size()).goe(1)) // 종료일 > 현재 시간 , 시작일 <= 현재시간
            .orderBy(community.proofList.size().desc()).limit(10) // 인증글 갯수대로 정렬 , 10개 출력
            .fetch(); //
  }

  public List<Community> endOfCommunity() {
    return queryFactory.selectFrom(community)
            .where(community.endDate.gt(String.valueOf(LocalDate.now())), (community.limitParticipants).gt(community.participantsList.size())) // 종료일 > 현재 시간 , 시작일 <= 현재시간
            .orderBy(community.endDate.asc(),community.participantsList.size().desc()).limit(10)
            .fetch();
  }


  private BooleanExpression titleEq(CommunitySearchCondition condition) {
    return StringUtils.hasText(condition.getTitle()) ? community.title.contains(condition.getTitle()) : null;
  }

}
