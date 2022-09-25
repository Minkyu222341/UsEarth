package sparta.seed.community.repository.customrepository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sparta.seed.community.domain.Community;
import sparta.seed.community.repository.customrepository.ProofRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static sparta.seed.community.domain.QCommunity.community;
import static sparta.seed.community.domain.QProof.proof;

@RequiredArgsConstructor
public class ProofRepositoryImpl implements ProofRepositoryCustom {
  private final JPAQueryFactory queryFactory;

  @Override
  public Long getCertifiedProof(Community find) {
    long result = queryFactory.selectFrom(proof)  // select * from proof p
            .leftJoin(proof.community, community) // 인증글 테이블에 있는 커뮤니티(컬럼)와 겹치는 커뮤니티를 가져와라
            .where(proof.heartList.size().goe(community.participantsList.size().divide(2)).and(proof.community.eq(find))) // 가져온 커뮤니티 중에 인증글의 좋아요갯수가 >= 커뮤니티 참가자의 인원수 /2 이고
            .fetchCount();                                                                                                // 해당인증글의 커뮤니티(컬럼)가 입력받은 커뮤니티와 같다면 그 갯수를 리턴해라
    return result;
  }


}
