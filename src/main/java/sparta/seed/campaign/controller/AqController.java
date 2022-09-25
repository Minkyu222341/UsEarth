package sparta.seed.campaign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sparta.seed.campaign.crawling.AirQualityApi;
import sparta.seed.campaign.domain.dto.responsedto.AqApiResponseDto;
import sparta.seed.campaign.service.AqService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AqController {
  private final AirQualityApi api;
  private final AqService aqService;

  @GetMapping("/api/community/api")
  public String apiTest(@RequestParam int timeIndex) throws IOException {
        api.saveApiData(timeIndex);
    return "ok";
  }

  @GetMapping("/api/community/airquality")
  public List<AqApiResponseDto> airQualityData() {
    return aqService.airQualityData();
  }
}
