package sparta.seed.campaign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.campaign.domain.dto.responsedto.AqApiResponseDto;
import sparta.seed.campaign.repository.AqRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AqService {

  private final AqRepository aqApiDataRepository;


  public AqApiResponseDto airQualityData(String category) {
    String datetime = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"));
    double average = aqApiDataRepository.airQualityData(category, datetime);
    String format = String.format("%.4f", average);
    System.out.println("[기준시각] ["+datetime+"]  [검색 카테고리] ["+category +"="+format+"]");
    AqApiResponseDto aqApiResponseDto = AqApiResponseDto.builder().category(category).amount(format).build();
    switch (category) {
      case "co":
        aqApiResponseDto.setCategory("일산화탄소");
        aqApiResponseDto.setMaxAmount("50");

        if(average < 2.1){
          aqApiResponseDto.setRisk("좋음");
        } else if (average < 9.1) {
          aqApiResponseDto.setRisk("보통");
        } else if (average < 15.1) {
          aqApiResponseDto.setRisk("나쁨");
        } else aqApiResponseDto.setRisk("매우 나쁨");
        break;
      case "o3":
        aqApiResponseDto.setCategory("오존");
        aqApiResponseDto.setMaxAmount("0.6");

        if(average < 0.031){
          aqApiResponseDto.setRisk("좋음");
        } else if (average < 0.091) {
          aqApiResponseDto.setRisk("보통");
        } else if (average < 0.151) {
          aqApiResponseDto.setRisk("나쁨");
        } else aqApiResponseDto.setRisk("매우 나쁨");
        break;
      case "no2":
        aqApiResponseDto.setCategory("이산화질소");
        aqApiResponseDto.setMaxAmount("2");

        if(average < 0.031){
          aqApiResponseDto.setRisk("좋음");
        } else if (average < 0.061) {
          aqApiResponseDto.setRisk("보통");
        } else if (average < 0.21) {
          aqApiResponseDto.setRisk("나쁨");
        } else aqApiResponseDto.setRisk("매우 나쁨");
        break;
      case "so2":
        aqApiResponseDto.setCategory("아황산가스");
        aqApiResponseDto.setMaxAmount("1");

        if(average < 0.021){
          aqApiResponseDto.setRisk("좋음");
        } else if (average < 0.051) {
          aqApiResponseDto.setRisk("보통");
        } else if (average < 0.151) {
          aqApiResponseDto.setRisk("나쁨");
        } else aqApiResponseDto.setRisk("매우 나쁨");
        break;
    }
    return aqApiResponseDto;
  }

}
