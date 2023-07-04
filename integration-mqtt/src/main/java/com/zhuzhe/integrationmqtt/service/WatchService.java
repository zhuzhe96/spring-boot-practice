package com.zhuzhe.integrationmqtt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.mqtt.handler.AsyncCallbackDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.GetNetworkAckPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.GetNetworkPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

// 异步回调模式：请求异步回调
@Slf4j
@Service
public class WatchService {
  public static final String group = "watch";
  public static final long TIMEOUT = 13000L;
  @Autowired private AsyncCallbackDispatchHandler handler;

  @Cacheable(cacheNames = "callCache", cacheManager = "callCacheManager", key = "#id")
  public void testCache(Long id) {
    // 像这个方法就可以使用注解实现缓存，而下面因为DeferredResult异步返回而不能使用缓存注解。
    log.info("方法开始执行，获取数据");
  }

  public DeferredResult<Network> getNetwork(String sn, String mac, String wifiName) {
    var result = new DeferredResult<Network>(TIMEOUT, () -> new RuntimeException("获取设备信息回调超时!"));
    log.info("开始通过MQTT调用设备端...");
    handler.sendMessage(
        group,
        sn,
        mac,
        new GetNetworkPayload(mac, wifiName),
        (bytes) -> {
          log.info("MQTT 回调处理");
          var getNetworkAckPayload =
              new ObjectMapper().readValue(bytes, GetNetworkAckPayload.class);
          log.info("MQTT 回调处理成功! Payload={}", getNetworkAckPayload);
          result.setResult(getNetworkAckPayload.getData());
        });
    return result;
  }
}
