package sparta.seed.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sparta.seed.campaign.crawling.AirQualityApi;
import sparta.seed.member.domain.Member;
import sparta.seed.member.repository.MemberRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
	private final AirQualityApi api;
	private final MemberRepository memberRepository;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void removeDailyMissions() {
		List<Member> allMembers = memberRepository.findAll();
		for (Member member : allMembers) {
			member.getDailyMission().clear();
		}
	}

	@Scheduled(cron = "0 30 * * * *")
	public void saveApiData() throws IOException, InterruptedException {
		for(int i=0; i<11;) {
			if(i == 10){
				api.saveApiData(-1);
			}
			try {
				api.saveApiData(0);
				break;
			} catch (Exception e) {
				i++;
				Thread.sleep(1000*60*2);
			}
		}
	}
}