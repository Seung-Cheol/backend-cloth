package project.store.common.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisUtil {
  private final StringRedisTemplate template;
  private final RedisTemplate<String, Object> redisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    return valueOperations.get(key);
  }

  public List<String> getListData(String key) {
    return (List<String>) redisTemplate.opsForValue().get(key);
  }

  public boolean existData(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    Duration expireDuration = Duration.ofSeconds(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void setListData(String key, String value, long duration) {
    if(existData(key)) {
      List<String> arr  = getListData(key);
      arr.add(value);
      redisTemplate.opsForValue().set(key, arr, duration, TimeUnit.DAYS);
    } else {
     List<String> arr = new ArrayList<>();
     arr.add(value);
     redisTemplate.opsForValue().set(key, arr, duration, TimeUnit.DAYS);
    }
  }

  public void deleteListData(String key, String value, long duration) {
    if(existData(key)) {
      List<String> arr  = getListData(key);
      arr.remove(value);
      redisTemplate.opsForValue().set(key, arr, duration, TimeUnit.DAYS);
    }
  }

  public void deleteData(String key) {
    template.delete(key);
  }
}