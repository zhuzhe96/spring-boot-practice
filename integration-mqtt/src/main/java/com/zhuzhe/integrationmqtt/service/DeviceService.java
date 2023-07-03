package com.zhuzhe.integrationmqtt.service;

import com.zhuzhe.integrationmqtt.entity.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Service
@SuppressWarnings("unused")
public class DeviceService {

  public static final long TIMEOUT = 3000L;

  // 大量更新请求,做锁机制排队
  public boolean add(Device device) {
    log.info("add device");
    return true;
  }

  // 缓存3s查询请求,实现瞬时流量消峰
  @Cacheable(cacheNames = "callCache", cacheManager = "callCacheManager", key = "#id")
  public Device get(Long id) {
    log.info("get device");
    return new Device(21, 100000000024L, "2C3AB39FE78D", true, true);
  }

  public DeferredResult<Device> getCallback(Long id) {
    var result =
        new DeferredResult<Device>(
            TIMEOUT,
            () -> {
              log.info("获取设备信息回调超时");
              return new RuntimeException("获取设备信息回调超时!");
            });
    log.info("开始通过MQTT调用设备端...");
    result.setResult(null);
    return result;
  }
}
