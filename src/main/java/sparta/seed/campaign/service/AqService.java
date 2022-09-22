package sparta.seed.campaign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.campaign.repository.AqRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AqService {

  private final AqRepository aqApiDataRepository;


  public String airQualityData(String category) {
    String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"));
    System.out.println("현재 시간 : "+datetime);
    Double average = aqApiDataRepository.airQualityData(category, datetime);
    String format = String.format("%.4f", average);
    return "[기준시각] ["+datetime+"]  [검색 카테고리] ["+category +"="+format+"]";
  }

}
