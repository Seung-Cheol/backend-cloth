package project.store.order.common.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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


  public boolean existData(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public boolean decreaseStock(String key, int count) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    int stock = Integer.parseInt(valueOperations.get(key));
    if (stock < count) {
      return false;
    }
    valueOperations.decrement(key, count);
    return true;
  }

  public boolean increaseStock(String key, int count) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    valueOperations.increment(key, count);
    return true;
  }

  public void setData(String key, String value) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    valueOperations.set(key, value);
  }

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = template.opsForValue();
    return valueOperations.get(key);
  }

  public void deleteData(String key) {
    template.delete(key);
  }
}