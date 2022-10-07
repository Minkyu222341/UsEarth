package sparta.seed.campaign.crawling;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import sparta.seed.campaign.domain.Campaign;
import sparta.seed.campaign.repository.CampaignRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CrawlingV1 {
  private WebDriver driver;

  private final CampaignRepository campaignRepository;

  private static final String url = "https://www.greenpeace.org/korea/?s=";


  public void process(){
//    System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
    System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("headless");
    driver = new ChromeDriver(options);
    try {
      campaignRepository.deleteAll();
      getDataList();
      Thread.sleep(200000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    driver.quit();  //브라우저 닫기
  }


  /**
   * data가져오기
   */
  private List<Campaign> getDataList() throws InterruptedException {
    List<Campaign> list = new ArrayList<>();

    driver.get(url);
    Thread.sleep(5000);

    for (int i = 0; i < 50; i++) {
      Thread.sleep(800);
      driver.findElement(By.cssSelector("body > div.outer_block_container > section.advanced-search > div > div.multiple-search-result > div.has-load-more > button")).sendKeys(Keys.ENTER);
      Thread.sleep(800);
    }

    Thread.sleep(10000);
    List<WebElement> contents = driver.findElements(By.cssSelector("body > div.outer_block_container > section.advanced-search > div > div.multiple-search-result > div.results-list > a"));
    for (WebElement content : contents) {
      System.out.println("----------------------------------------------------------");
      String text = content.findElement(By.className("meta-box")).getText(); // 카테고리
      String pTag = content.findElement(By.tagName("P")).getText(); // 제목
      String imageLink = content.getAttribute("href");
      String attribute = content.findElement(By.tagName("div")).getAttribute("style");
      String replace = attribute.replace("-150x150", "");
      String[] imageUrl = replace.split("\"");  // 이미지url

      if (imageUrl[1] != null) {
        Campaign campaign = Campaign.builder()
                .thumbnail(imageUrl[1])
                .title(pTag)
                .thumbnailUrl(imageLink)
                .build();

        list.add(campaign);
      }
    }
    campaignRepository.saveAll(list);

    return list;
  }
}
