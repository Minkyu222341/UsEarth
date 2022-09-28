package sparta.seed.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
  private final RedisTemplate<String, String> redisTemplate;

  public void setValues(String key, String data, Duration duration) {
    ValueOperations<String, String> values = redisTemplate.opsForValue();
    values.set(key, data, duration);
  }

  public void deleteValues(String key) {
    redisTemplate.delete(key);
  }

  public void addSlang(String data) {
    SetOperations<String, String> values = redisTemplate.opsForSet();
    values.add("slang", data);
  }

  public Set<String> getSlangSet() {
    SetOperations<String, String> values = redisTemplate.opsForSet();
    return values.members("slang");
  }

  public void addMission(String memberId , String missionId) {
    SetOperations<String, String> values = redisTemplate.opsForSet();
    values.add("owner_"+memberId, missionId);
  }

  public Set<String> getMissionSet(String memberId) {
    SetOperations<String, String> values = redisTemplate.opsForSet();
    return values.members("owner_"+memberId);
  }

  public void deleteMissionSet(String memberId) {
    redisTemplate.delete("owner_"+memberId);
  }
}
