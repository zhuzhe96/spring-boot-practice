package com.zhuzhe.securityrbac.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLockUtil {
  public static final int RETRY_COUNT = 4;
  public static final long RETRY_EXPIRE_TIME = 10;
  public static final String RETRY_PREFIX = "retry:user:";
  public static final String LOCK_PREFIX = "lock:user:";
  @Autowired private RedisUtil redisUtil;

  public int getRetryCount(Long userId) {
    long ttl =  RETRY_EXPIRE_TIME;
    String retryKey = RETRY_PREFIX + userId;
    Integer retryCount = redisUtil.getCacheObject(retryKey);

    // 如果当前用户已经锁定了,则不能进行重试
    if (getLock(userId)) return 0;

    if (retryCount==null){
      retryCount = RETRY_COUNT;
    }else {
      retryCount--;
      if (retryCount<=0){
        setLock(userId);
      }
      ttl = redisUtil.getExpire(retryKey);
    }
    redisUtil.setCacheObject(retryKey, retryCount, ttl, TimeUnit.MINUTES);
    return retryCount;
  }

  public void setLock(Long userId) {
    String lockKey = LOCK_PREFIX + userId;
    redisUtil.setCacheObject(lockKey, 1, 3L, TimeUnit.HOURS);
  }

  public boolean getLock(Long userId) {
    String lockKey = LOCK_PREFIX + userId;
    return redisUtil.hasKey(lockKey);
  }
}
