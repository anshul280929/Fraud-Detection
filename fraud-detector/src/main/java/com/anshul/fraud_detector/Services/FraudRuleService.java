package com.anshul.fraud_detector.Services;

import com.anshul.fraud_detector.domain.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class FraudRuleService {
    private final StringRedisTemplate redis;


    public FraudRuleService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public static record Decision(boolean flag, String reason){

    }

    public Decision evaluate(User user, String location, String deviceId, double amount, Instant ts) {
        // Rule 1: High value + outside home city
        if (amount > 50_000 && user.getCity() != null && !user.getCity().equalsIgnoreCase(location)) {
            return new Decision(true, "High-value outside usual city");
        }
        // Rule 2: Velocity > 3 txns within 60s
        if (isVelocityHigh(user.getUserId(), ts)) {
            return new Decision(true, "High velocity in 60s window");
        }
        // Rule 3: Device used by multiple users recently (e.g., 15m)
        if (isDeviceMultiUser(deviceId, user.getUserId())) {
            return new Decision(true, "Shared device across users");
        }

        return new Decision(false, null);

    }

    private boolean isVelocityHigh(String userId, Instant ts) {
        String key = "user:%s:txns".formatted(userId);
        long now = ts.getEpochSecond();
        long windowStart = now - 60;

        // remove old
        redis.opsForZSet().removeRangeByScore(key, 0, windowStart);
        // add current
        redis.opsForZSet().add(key, String.valueOf(now), now);
        // expire to auto-cleanup
        redis.expire(key, 10, TimeUnit.MINUTES);

        Long count = redis.opsForZSet().count(key, windowStart, now);
        return count != null && count > 3;
    }

    private boolean isDeviceMultiUser(String deviceId, String userId) {
        String key = "device:%s:users".formatted(deviceId);
        redis.opsForSet().add(key, userId);
        redis.expire(key, 15, TimeUnit.MINUTES);

        Long size = redis.opsForSet().size(key);
        return size != null && size > 1;
    }
}
