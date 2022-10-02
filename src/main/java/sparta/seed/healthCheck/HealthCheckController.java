package sparta.seed.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
  /**
   헬스체크 컨트롤러
   */
  @GetMapping("/user/health")
  public String checkHealth() {
    return "healthyV2";
  }
}
