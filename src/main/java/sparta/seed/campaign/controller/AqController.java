package sparta.seed.campaign.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.seed.campaign.crawling.AirQualityApi;
import sparta.seed.campaign.service.AqService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class AqController {
  private final AirQualityApi api;
  private final AqService aqService;

  @GetMapping("/api/community/api")
  public String apiTest() throws IOException {
    String[] itemList = {"co", "o3", "no2", "so2", "pm10", "pm25"};
    for (String item : itemList) {
      api.saveApiData(item);
    }
    return "ok";
  }

  @GetMapping("/api/community/airquality")
  public String airQualityData(String category) {
    return aqService.airQualityData(category);
  }
}
