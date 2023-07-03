package com.zhuzhe.integrationmqtt.mqtt.handler.callback;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class AbstractMessageDispatcher implements DeviceMessageDispatcher {

  // 创建临时存储回调函数的map
  private final Cache<String, MessageCallback> callbackMap =
      Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();

  // 创建处理设备调用和云端调用的线程池
  private final ScheduledExecutorService executorService =
      new ScheduledThreadPoolExecutor(
          8,
          tf -> {
            var thread = new Thread("mqtt-message-dispatcher");
            thread.setUncaughtExceptionHandler(
                (t, e) -> log.error("mqtt-message-dispatcher execute error", e));
            return thread;
          });

  @Override
  public void sendMessage(String mac, MqttMessage message, MessageCallback callback) {
    message.getPayload();
    
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleMessage(String mac, MqttMessage message) {

  }
}
