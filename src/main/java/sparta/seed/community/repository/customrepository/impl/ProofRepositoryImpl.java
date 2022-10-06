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
  public long countOfCertifiedProofByOnePeople(Community find) {
    long result = queryFactory.selectFrom(proof)
            .leftJoin(proof.community, community)
            .where(proof.heartList.size().goe(1).and(proof.community.eq(find)))
            .fetchCount();
    return result;
  }

  @Override
  public long countOfCertifiedProofByMoreThanTwoPeople(Community find) {
    long result = queryFactory.selectFrom(proof)
            .leftJoin(proof.community, community)
            .where(proof.heartList.size().goe(2).and(proof.community.eq(find)))
            .fetchCount();
    return result;
  }
}
