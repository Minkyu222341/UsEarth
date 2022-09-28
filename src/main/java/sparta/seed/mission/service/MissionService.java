package sparta.seed.mission.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.exception.CustomException;
import sparta.seed.exception.ErrorCode;
import sparta.seed.member.domain.Member;
import sparta.seed.member.repository.MemberRepository;
import sparta.seed.mission.domain.ClearMission;
import sparta.seed.mission.domain.Mission;
import sparta.seed.mission.domain.dto.requestdto.MissionRequestDto;
import sparta.seed.mission.domain.dto.responsedto.MissionClearResponseDto;
import sparta.seed.mission.domain.dto.responsedto.MissionDetailResponseDto;
import sparta.seed.mission.domain.dto.responsedto.MissionResponseDto;
import sparta.seed.mission.repository.ClearMissionRepository;
import sparta.seed.mission.repository.MissionRepository;
import sparta.seed.sercurity.UserDetailsImpl;
import sparta.seed.util.DateUtil;
import sparta.seed.util.ExpUtil;
import sparta.seed.util.RedisService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;



@Service
@RequiredArgsConstructor
public class MissionService {
  private final MissionRepository missionRepository;
  private final MemberRepository memberRepository;
  private final ClearMissionRepository clearMissionRepository;
  private final RedisService redisService;
  private final ExpUtil expUtil;


  /**
   * 미션 생성 - 관리자
   */
  public Mission crateMission(MissionRequestDto missionRequestDto) {
    Mission mission = Mission.builder().content(missionRequestDto.getMissionName()).build();
    missionRepository.save(mission);
    return mission;
  }

  /**
   * 유저한테 랜덤 미션 5개 넣어주기 (비워주는건 스케줄러 연동)
   */
  @Transactional
  public MissionResponseDto injectMission(UserDetailsImpl userDetails) {
    if (userDetails != null) {
      Member loginMember = memberRepository.findById(userDetails.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_USER));

      int memberLevel = loginMember.getLevel();

      Map<String, Boolean> dailyMission = loginMember.getDailyMission();

      while (dailyMission.size() < 5) {
        Mission mission = missionRepository.findById((long) ((Math.random() * missionRepository.count())+1)).get();
        String missionId = String.valueOf(mission.getId());
        Set<String> missionSet = redisService.getMissionSet(String.valueOf(userDetails.getId()));

        if(!missionSet.contains(missionId)){
          if(memberLevel < 4){
            if(mission.getContent().startsWith("[하]")){
              dailyMission.put(mission.getContent(), false);
              redisService.addMission(String.valueOf(userDetails.getId()), missionId);
            }
          } else if (memberLevel < 8 ) {
            if(mission.getContent().startsWith("[하]") || mission.getContent().startsWith("[중]")) {
              dailyMission.put(mission.getContent(), false);
              redisService.addMission(String.valueOf(userDetails.getId()), missionId);
            }
          }else {
            dailyMission.put(mission.getContent(), false);
            redisService.addMission(String.valueOf(userDetails.getId()), missionId);
          }

        }
      }

      MissionResponseDto missionResponseDto = MissionResponseDto.builder()
              .memberId(userDetails.getId())
              .build();

      for (String key : dailyMission.keySet()) {
        boolean value = dailyMission.get(key);
        MissionDetailResponseDto missionDetailResponseDto = new MissionDetailResponseDto(key.substring(3), key.substring(0, 3), value);
        missionResponseDto.addMisson(missionDetailResponseDto);
      }

      return missionResponseDto;
    } else throw new CustomException(ErrorCode.UNKNOWN_ERROR);
  }

   /**
   * 레디스 중복미션 제
   */
  public void deleteMissionSet(String memberId){
    redisService.deleteMissionSet(memberId);
  }

  /**
   * 미션 완료
   */
  @Transactional
  public MissionClearResponseDto completeMission(UserDetailsImpl userDetails, MissionRequestDto missionRequestDto) {

    String difficulty = missionRequestDto.getDifficulty();
    String clearMissionName = difficulty + missionRequestDto.getMissionName();
    Member loginMember = memberRepository.findById(userDetails.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.UNKNOWN_USER));
    ClearMission clearMission = ClearMission.builder()
            .memberId(userDetails.getId())
            .content(missionRequestDto.getMissionName())
            .clearTime(LocalDate.now())
            .build();

    if (!loginMember.getDailyMission().get(clearMissionName)) {
      loginMember.getDailyMission().put(clearMissionName, true);
      clearMissionRepository.save(clearMission);

      Integer needNextLevelExpBeforeAddExp = expUtil.getNextLevelExp().get(loginMember.getLevel());

      if (difficulty.equals("[상]")) {
        loginMember.addExp(3);
        currentExpCompareToNeedNextLevelExp(loginMember, needNextLevelExpBeforeAddExp);
      } else if (difficulty.equals("[중]")) {
        loginMember.addExp(2);
        currentExpCompareToNeedNextLevelExp(loginMember, needNextLevelExpBeforeAddExp);
      } else {
        loginMember.addExp(1);
        currentExpCompareToNeedNextLevelExp(loginMember, needNextLevelExpBeforeAddExp);
      }
      Integer needNextLevelExpAfterAddExp = expUtil.getNextLevelExp().get(loginMember.getLevel());

      return MissionClearResponseDto.builder()
              .missionName(missionRequestDto.getMissionName())
              .complete(true)
              .level(loginMember.getLevel())
              .nextLevelExp(loginMember.getExp())
              .needNextLevelExp(needNextLevelExpAfterAddExp)
              .build();
    } else throw new CustomException(ErrorCode.NOT_FOUND_MISSION);
  }

  private void currentExpCompareToNeedNextLevelExp(Member loginMember, Integer needNextLevelExpBeforeAddExp) {
    if (loginMember.getExp() == needNextLevelExpBeforeAddExp) {
      loginMember.levelUp();
      loginMember.initExp();
    } else if (loginMember.getExp() > needNextLevelExpBeforeAddExp) {
      loginMember.levelUp();
      loginMember.minusExp(needNextLevelExpBeforeAddExp);
    }
  }


}

