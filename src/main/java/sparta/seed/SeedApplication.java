package sparta.seed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class SeedApplication {
//  public static void main(String[] args) {
//    SpringApplication.run(SeedApplication.class, args);
//  }

  @PostConstruct
  public void started(){
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
  public static final String APPLICATION_LOCATIONS = "spring.config.location="
          + "classpath:application.properties,"
          + "/application.yml";

  public static void main(String[] args) {
    new SpringApplicationBuilder(SeedApplication.class)
            .properties(APPLICATION_LOCATIONS)
            .run(args);
  }

}
