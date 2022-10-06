package sparta.seed.community.repository.customrepository;


import sparta.seed.community.domain.Community;

public interface ProofRepositoryCustom {
  long countOfCertifiedProofByOnePeople(Community community);

  long countOfCertifiedProofByMoreThanTwoPeople(Community community);

}
