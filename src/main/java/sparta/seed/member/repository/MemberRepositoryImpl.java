package sparta.seed.member.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sparta.seed.community.domain.Community;
import sparta.seed.community.domain.QCommunity;
import sparta.seed.community.domain.QParticipants;
import sparta.seed.community.domain.dto.responsedto.CommunityMyJoinResponseDto;

import java.util.ArrayList;
import java.util.List;

import static sparta.seed.community.domain.QCommunity.*;
import static sparta.seed.community.domain.QParticipants.*;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public List<Community> getCommunityBelongToMember(Long memberId) {
    return queryFactory.select(community)
            .from(participants)
            .where(participants.memberId.eq(memberId))
            .join(participants.community, community)
            .orderBy(community.id.desc())
            .fetch();
  }

}
