package sparta.seed.campaign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.seed.campaign.domain.dto.responsedto.AqApiResponseDto;
import sparta.seed.campaign.repository.AqRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AqService {

  private final AqRepository aqApiDataRepository;


  public List<AqApiResponseDto> airQualityData() {
    String[] categoryList = {"co","o3","no2","so2"};
    String datetime = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00"));

    List<AqApiResponseDto> aqApiResponseDtoList = new ArrayList<>();

    for (String category : categoryList) {
      double average = aqApiDataRepository.airQualityData(category, datetime);
      String format = String.format("%.4f", average);
      AqApiResponseDto aqApiResponseDto = AqApiResponseDto.builder().category(category).amount(format).build();
      switch (category) {
        case "co":
          aqApiResponseDto.setCategory("일산화탄소");
          if (average < 2.01) {
            aqApiResponseDto.setRisk("좋음");
            aqApiResponseDto.setMaxAmount("2.0000");
          } else if (average < 9.01) {
            aqApiResponseDto.setRisk("보통");
            aqApiResponseDto.setMaxAmount("9.0000");
          } else if (average < 15.01) {
            aqApiResponseDto.setRisk("나쁨");
            aqApiResponseDto.setMaxAmount("15.0000");
          } else {
            aqApiResponseDto.setRisk("매우 나쁨");
            aqApiResponseDto.setMaxAmount("50.0000");
          }
          break;

        case "o3":
          aqApiResponseDto.setCategory("오존");
          if (average < 0.031) {
            aqApiResponseDto.setRisk("좋음");
            aqApiResponseDto.setMaxAmount("0.0300");
          } else if (average < 0.091) {
            aqApiResponseDto.setRisk("보통");
            aqApiResponseDto.setMaxAmount("0.0900");
          } else if (average < 0.151) {
            aqApiResponseDto.setRisk("나쁨");
            aqApiResponseDto.setMaxAmount("0.1510");
          } else {
            aqApiResponseDto.setRisk("매우 나쁨");
            aqApiResponseDto.setMaxAmount("0.6000");
          }
          break;

        case "no2":
          aqApiResponseDto.setCategory("이산화질소");
          if (average < 0.031) {
            aqApiResponseDto.setRisk("좋음");
            aqApiResponseDto.setMaxAmount("0.0300");
          } else if (average < 0.061) {
            aqApiResponseDto.setRisk("보통");
            aqApiResponseDto.setMaxAmount("0.0600");
          } else if (average < 0.201) {
            aqApiResponseDto.setRisk("나쁨");
            aqApiResponseDto.setMaxAmount("0.2000");
          } else {
            aqApiResponseDto.setRisk("매우 나쁨");
            aqApiResponseDto.setMaxAmount("2.0000");
          }
          break;

        case "so2":
          aqApiResponseDto.setCategory("아황산가스");
          if (average < 0.0201) {
            aqApiResponseDto.setRisk("좋음");
            aqApiResponseDto.setMaxAmount("0.0200");
          } else if (average < 0.0501) {
            aqApiResponseDto.setRisk("보통");
            aqApiResponseDto.setMaxAmount("0.0500");
          } else if (average < 0.1501) {
            aqApiResponseDto.setRisk("나쁨");
            aqApiResponseDto.setMaxAmount("0.1500");
          } else {
            aqApiResponseDto.setRisk("매우 나쁨");
            aqApiResponseDto.setMaxAmount("1.0000");
          }
          break;
      }
      aqApiResponseDtoList.add(aqApiResponseDto);
    }
    return aqApiResponseDtoList;
  }

}
