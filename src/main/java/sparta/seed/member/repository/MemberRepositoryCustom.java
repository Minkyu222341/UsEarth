package sparta.seed.member.repository;

import sparta.seed.community.domain.Community;

import java.util.List;

public interface MemberRepositoryCustom {
  List<Community> getCommunityBelongToMember(Long memberId);
}
