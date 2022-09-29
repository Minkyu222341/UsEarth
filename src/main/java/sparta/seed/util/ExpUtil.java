package sparta.seed.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ExpUtil {
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
