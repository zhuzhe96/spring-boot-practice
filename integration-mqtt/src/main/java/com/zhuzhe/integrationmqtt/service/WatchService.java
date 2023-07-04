package com.zhuzhe.integrationmqtt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.entity.Watch;
import com.zhuzhe.integrationmqtt.mqtt.handler.AsyncCallbackDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.GetNetworkAckPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.GetNetworkPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.SetNetworkAckPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.SetNetworkPayload;
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
  public Watch get(Long id) {
    // 像这个方法就可以使用注解实现缓存，而下面因为DeferredResult异步返回而不能使用缓存注解。
    log.info("模拟查询数据库获取设备信息...");
    return new Watch(1, "2022000000000001785", "036915CC0CBF", false, true, null);
  }

  // 云端 -> 设备端: 发送并异步回调
  public DeferredResult<Network> getNetwork(String sn, String mac, String wifiName) {
    var result = new DeferredResult<Network>(TIMEOUT, () -> new RuntimeException("获取设备网络信息超时!"));
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

  public DeferredResult<Object> setNetwork(String sn, String mac, Network network){
    var result = new DeferredResult<>(TIMEOUT, () -> new RuntimeException("设置设备网络信息超时!"));
    log.info("开始通过MQTT调用设备端...");
    handler.sendMessage(
        group,
        sn,
        mac,
        new SetNetworkPayload(mac, network),
        (bytes) -> {
          log.info("MQTT 回调处理");
          var setNetworkAckPayload =
              new ObjectMapper().readValue(bytes, SetNetworkAckPayload.class);
          if (!MqttStatus.isSuccess(setNetworkAckPayload.getStatus())){
            result.setResult(MqttStatus.FAILURE);
          }
          log.info("MQTT 回调处理成功! Payload={}", setNetworkAckPayload);
          result.setResult(MqttStatus.SUCCESS);
        });
    return result;
  }
}
