package sparta.seed.util;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ExpUtil {
  //열흘 기준으로 1~11 하루에 5개니까 50개
//          12 ~ 1
//          34 ~ 2
//          567 ~ 3
//          8910 ~ 4
//          11 ~ 5
  public HashMap<Integer, Integer> getNextLevelExp() {
    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(1, 1);
    map.put(2, 1);
    map.put(3, 2);
    map.put(4, 5);
    map.put(5, 5);
    map.put(6, 6);
    map.put(7, 8);
    map.put(8, 8);
    map.put(9, 10);
    map.put(10, 12);
    return map;
  }


}
