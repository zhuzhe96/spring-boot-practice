package com.zhuzhe.integrationmqtt.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfig {
  // 自定义缓存过滤器，设定10s缓存用于限流
  @Bean
  public CacheManager callCacheManager(){
    var manager = new CaffeineCacheManager("callCache");
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS);
    manager.setCaffeine(caffeine);
    return manager;
  }
}
