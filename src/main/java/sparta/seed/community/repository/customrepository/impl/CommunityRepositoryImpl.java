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

import java.time.LocalDate;
import java.util.List;

import static sparta.seed.community.domain.QCommunity.community;


@RequiredArgsConstructor
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public QueryResults<Community> getAllCommunity(Pageable pageable, CommunitySearchCondition condition) {
    return queryFactory
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
            .where(community.endDate.goe(String.valueOf(LocalDate.now())), (community.proofList.size()).goe(1))
            .orderBy(community.proofList.size().desc()).limit(10)
            .fetch();
  }

  public List<Community> endOfCommunity() {
    return queryFactory.selectFrom(community)
            .where(community.startDate.gt(String.valueOf(LocalDate.now())), (community.limitParticipants).gt(community.participantsList.size()))
            .orderBy(community.startDate.asc(),community.participantsList.size().desc()).limit(10)
            .fetch();
  }


  private BooleanExpression titleEq(CommunitySearchCondition condition) {
    return StringUtils.hasText(condition.getTitle()) ? community.title.contains(condition.getTitle()) : null;
  }

}
