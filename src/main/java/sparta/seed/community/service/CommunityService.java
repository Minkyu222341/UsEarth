package sparta.seed.community.service;

import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sparta.seed.community.domain.Community;
import sparta.seed.community.domain.Participants;
import sparta.seed.community.domain.dto.requestdto.CommunityRequestDto;
import sparta.seed.community.domain.dto.requestdto.CommunitySearchCondition;
import sparta.seed.community.domain.dto.responsedto.CommunityAllResponseDto;
import sparta.seed.community.domain.dto.responsedto.CommunityResponseDto;
import sparta.seed.community.domain.dto.responsedto.ParticipantResponseDto;
import sparta.seed.community.repository.CommunityRepository;
import sparta.seed.community.repository.ParticipantsRepository;
import sparta.seed.community.repository.ProofRepository;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.jwt.TokenProvider;
import sparta.seed.msg.ResponseMsg;
import sparta.seed.s3.S3Uploader;
import sparta.seed.sercurity.UserDetailsImpl;
import sparta.seed.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

  private final CommunityRepository communityRepository;
  private final S3Uploader s3Uploader;
  private final ParticipantsRepository participantsRepository;
  private final DateUtil dateUtil;
  private final ProofRepository proofRepository;
  private final TokenProvider tokenProvider;
  private final SlangService slangService;

  /**
   * 캠페인 전체 조회
   */
  public ResponseEntity<Slice<CommunityAllResponseDto>> getAllCommunity(Pageable pageable, CommunitySearchCondition condition, UserDetailsImpl userDetails, HttpServletRequest servletRequest) throws ParseException {

    tokenProvider.validateHttpHeader(servletRequest);

    QueryResults<Community> allCommunity = communityRepository.getAllCommunity(pageable, condition);
    List<CommunityAllResponseDto> allCommunityList = getAllCommunityList(allCommunity, userDetails);
    boolean hasNext = hasNextPage(pageable, allCommunityList);
    SliceImpl<CommunityAllResponseDto> communityResponseDtos = new SliceImpl<>(allCommunityList, pageable, hasNext);
    return ResponseEntity.ok().body(communityResponseDtos);
  }

  /**
   * 캠페인 작성
   */
  public ResponseEntity<String> createCommunity(CommunityRequestDto requestDto, MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
    Long loginUserId = userDetails.getId();
    String nickname = isChangedNickname(requestDto, userDetails);
    Community community = createCommunity(requestDto, multipartFile, loginUserId, nickname);
    Participants groupLeader = getGroupLeader(loginUserId, nickname, community);

    communityRepository.save(community);
    participantsRepository.save(groupLeader);

    return ResponseEntity.ok().body(ResponseMsg.WRITE_SUCCESS.getMsg());

  }

  /**
   * 캠페인 상세 조회
   */
  public ResponseEntity<CommunityResponseDto> getDetailCommunity(Long id, UserDetailsImpl userDetails, HttpServletRequest servletRequest) throws ParseException {

    tokenProvider.validateHttpHeader(servletRequest);

    Community community = findTheCommunityByMemberId(id);
    Long certifiedProof = getCertifiedProof(community);
    CommunityResponseDto communityResponseDto = CommunityResponseDto.builder()
            .communityId(community.getId())
            .createAt(String.valueOf(community.getCreatedAt()))
            .nickname(community.getNickname())
            .title(community.getTitle())
            .content(community.getContent())
            .img(community.getImg())
            .participantsCnt(community.getParticipantsList().size())
            .limitParticipants(community.getLimitParticipants())
            .participant(userDetails != null && participant(userDetails, community))
            .limitScore(community.getLimitScore())
            .currentPercent(getCurrentPercent(community))
            .successPercent(getSuccessPercent(community, certifiedProof))
            .currentCertifiedProof(certifiedProof)
            .startDate(community.getStartDate())
            .endDate(community.getEndDate())
            .dateStatus(getDateStatus(community))
            .secret(community.isPasswordFlag())
            .password(community.getPassword())
            .writer(userDetails != null && community.getMemberId().equals(userDetails.getId()))
            .build();
    return ResponseEntity.ok().body(communityResponseDto);
  }

  /**
   * 캠페인 업데이트
   */
  @Transactional
  public ResponseEntity<String> updateCommunity(Long id, CommunityRequestDto communityRequestDto,
                                                MultipartFile multipartFile, UserDetailsImpl userDetails) throws IOException {
    Community community = findTheCommunityByMemberId(id);
    String nickname = isChangedNickname(communityRequestDto, userDetails);
    if (userDetails != null && community.getMemberId().equals(userDetails.getId())) {

      slangService.checkSlang(communityRequestDto.getTitle());
      slangService.checkSlang(communityRequestDto.getContent());

      community.update(communityRequestDto,nickname);

      if (communityRequestDto.isDelete() || multipartFile != null) {
        community.setImg(returnImageUrl(multipartFile));
      }

      return ResponseEntity.ok().body(ResponseMsg.UPDATE_SUCCESS.getMsg());
    }
    throw new CustomException(ErrorCode.INCORRECT_USERID);
  }

  /**
   * 캠페인 삭제
   */
  public ResponseEntity<String> deleteCommunity(Long id, UserDetailsImpl userDetails) {
    Community community = findTheCommunityByMemberId(id);
    if (validateWriter(userDetails, community)) {
      communityRepository.deleteById(id);
    }
    return ResponseEntity.ok().body(ResponseMsg.DELETED_SUCCESS.getMsg());
  }

  /**
   * 캠페인 참가
   */
  @Transactional
  public ResponseEntity<String> joinMission(Long id, UserDetailsImpl userDetails) {
    Community community = findTheCommunityByMemberId(id);
    if (userDetails != null) {
      if (community.getMemberId().equals(userDetails.getId()) || participantsRepository.existsByCommunityAndMemberId(community, userDetails.getId())) {
        throw new CustomException(ErrorCode.ALREADY_PARTICIPATED);
      }
      if (community.getParticipantsList().size() >= community.getLimitParticipants()) {
        throw new CustomException(ErrorCode.EXCESS_PARTICIPANT);
      }
      Participants participants = Participants.builder()
              .community(community)
              .memberId(userDetails.getId())
              .nickname(userDetails.getNickname())
              .build();
      community.addParticipant(participants);
      participantsRepository.save(participants);
      return ResponseEntity.ok().body(ResponseMsg.JOIN_SUCCESS.getMsg());
    }
    throw new CustomException(ErrorCode.UNKNOWN_USER);
  }

  /**
   * 캠페인 참여자 리스트 조회
   */
  public ResponseEntity<List<ParticipantResponseDto>> getParticipantsList(Long id) {
    Community community = findTheCommunityByMemberId(id);
    List<Participants> participantsList = community.getParticipantsList();
    List<ParticipantResponseDto> responseDtoList = new ArrayList<>();
    for (Participants participants : participantsList) {
      responseDtoList.add(ParticipantResponseDto.builder()
              .id(participants.getId())
              .nickname(participants.getNickname())
              .build());
    }
    return ResponseEntity.ok().body(responseDtoList);
  }

  /**
   * 캠페인 인기그룹
   */
  public ResponseEntity<List<CommunityAllResponseDto>> activeCommunity(UserDetailsImpl userDetails) throws ParseException {
    List<Community> communities = communityRepository.activeCommunity();
    List<CommunityAllResponseDto> communityList = getCommunityAllResponseDtos(communities, userDetails);
    return ResponseEntity.ok().body(communityList);
  }

  /**
   * 캠페인 종료임박 그룹
   */
  public ResponseEntity<List<CommunityAllResponseDto>> endOfCommunity(UserDetailsImpl userDetails) throws ParseException {
    List<Community> communities = communityRepository.endOfCommunity();
    List<CommunityAllResponseDto> communityList = getCommunityAllResponseDtos(communities, userDetails);
    return ResponseEntity.ok().body(communityList);
  }

  //공용메소드

  private boolean hasNextPage(Pageable pageable, List<CommunityAllResponseDto> CommunityList) {
    boolean hasNext = false;
    if (CommunityList.size() > pageable.getPageSize()) {
      CommunityList.remove(pageable.getPageSize());
      hasNext = true;
    }
    return hasNext;
  }
  private List<CommunityAllResponseDto> getAllCommunityList(QueryResults<Community> allCommunity, UserDetailsImpl
          userDetails) throws ParseException {
    List<CommunityAllResponseDto> communityList = new ArrayList<>();
    for (Community community : allCommunity.getResults()) {
      Long certifiedProof = getCertifiedProof(community);
      communityList.add(CommunityAllResponseDto.builder()
              .communityId(community.getId())
              .nickname(community.getNickname())
              .title(community.getTitle())
              .img(community.getImg())
              .currentPercent(getCurrentPercent(community))
              .successPercent(getSuccessPercent(community, certifiedProof))
              .dateStatus(getDateStatus(community))
              .secret(community.isPasswordFlag())
              .password(community.getPassword())
              .writer(userDetails != null && community.getMemberId().equals(userDetails.getId()))
              .build());
    }
    return communityList;
  }

  private Boolean participant(UserDetailsImpl userDetails, Community community) {
    return participantsRepository.existsByCommunityAndMemberId(community, userDetails.getId());
  }

  private Community createCommunity(CommunityRequestDto requestDto, MultipartFile multipartFile, Long loginUserId, String nickname) throws IOException {

    slangService.checkSlang(requestDto.getTitle());
    slangService.checkSlang(requestDto.getContent());

    return Community.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .img(returnImageUrl(multipartFile))
            .secret(requestDto.isSecret())
            .password(requestDto.getPassword())
            .memberId(loginUserId)
            .nickname(nickname)
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .limitParticipants(requestDto.getLimitParticipants())
            .limitScore(requestDto.getLimitScore())
            .build();
  }

  private Participants getGroupLeader(Long loginUserId, String nickname, Community community) {
    return Participants.builder()
            .community(community)
            .memberId(loginUserId)
            .nickname(nickname)
            .build();
  }

  private Community findTheCommunityByMemberId(Long id) {
    return communityRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMUNITY));
  }

  private Long getCertifiedProof(Community community) {
    return proofRepository.getCertifiedProof(community);
  }

  private Boolean validateWriter(UserDetailsImpl userDetails, Community community) {
    if (userDetails != null && community.getMemberId().equals(userDetails.getId())) {
      return true;
    } else throw new CustomException(ErrorCode.INCORRECT_USERID);
  }

  public String getDateStatus(Community community) throws ParseException {
    return dateUtil.dateStatus(community.getStartDate(), community.getEndDate());
  }

  private String returnImageUrl(MultipartFile multipartFile) throws IOException {
    String[] defaultImgList = {"https://usearth.s3.ap-northeast-2.amazonaws.com/usimg/defaultImg1.png",
            "https://usearth.s3.ap-northeast-2.amazonaws.com/usimg/defaultImg2.png",
            "https://usearth.s3.ap-northeast-2.amazonaws.com/usimg/defaultImg3.png",
            "https://usearth.s3.ap-northeast-2.amazonaws.com/usimg/defaultImg4.png"};

    if (multipartFile != null) {
      return s3Uploader.upload(multipartFile).getUploadImageUrl();
    } else return defaultImgList[(int) (Math.random() * 4)];
  }


  private List<CommunityAllResponseDto> getCommunityAllResponseDtos(List<Community> communities, UserDetailsImpl
          userDetails) throws ParseException {
    List<CommunityAllResponseDto> communityList = new ArrayList<>();
    for (Community community : communities) {
        Long certifiedProof = getCertifiedProof(community);
        communityList.add(CommunityAllResponseDto.builder()
                .communityId(community.getId())
                .nickname(community.getNickname())
                .title(community.getTitle())
                .img(community.getImg())
                .currentPercent(getCurrentPercent(community))
                .successPercent(getSuccessPercent(community, certifiedProof))
                .dateStatus(getDateStatus(community))
                .secret(community.isPasswordFlag())
                .password(community.getPassword())
                .writer(userDetails != null && community.getMemberId().equals(userDetails.getId()))
                .build());
      }
    return communityList;
  }

  private double getCurrentPercent(Community community) {
    return ((double) community.getParticipantsList().size() / community.getLimitParticipants()) * 100;
  }

  private double getSuccessPercent(Community community, Long certifiedProof) {
    return ((double)certifiedProof / community.getLimitScore()) * 100;
  }

  private String isChangedNickname(CommunityRequestDto requestDto, UserDetailsImpl userDetails) {
    String nickname;
    if (requestDto.getChangeNickname()== null) {
      nickname = userDetails.getNickname();
    } else {
      nickname = requestDto.getChangeNickname();
    }
    return nickname;
  }
}

