package com.zhuzhe.integrationmqtt.mqtt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zhuzhe.integrationmqtt.mqtt.MqttDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.config.MqttProperties;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

// 异步回调模式：消息处理者
@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncCallbackDispatchHandler extends MqttDispatchHandler {
  private final Cache<String, MessageCallback> cache =
      Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
  private final ScheduledExecutorService executorService =
      new ScheduledThreadPoolExecutor(
          8,
          runnable -> {
            var thread = new Thread(runnable);
            thread.setName("message-callback-thread");
            thread.setUncaughtExceptionHandler((t, e) -> log.error("消息处理发生异常! 原因: ", e));
            return thread;
          });
  private final MqttProperties properties;
  

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    try {
      var payload = message.getPayload();
      var messagePayload = new ObjectMapper().readValue(payload, MessagePayload.class);
      if (StringUtils.isBlank(messagePayload.getUrl()))
        throw new RuntimeException("无效的Mqtt消息, 原因: url为空!");

      switch (messagePayload.getUrl()) {
        case PayloadUrlStorage.NETWORK,
            PayloadUrlStorage.BACKGROUND,
            PayloadUrlStorage.TIMED_TASK -> {
          var token = messagePayload.getToken();
          var callback = cache.getIfPresent(token);
          if (callback == null) {
            log.info("找不到对应的回调处理方法!");
            return;
          }
          cache.invalidate(token);
          execute(payload, callback);
        }
      }

    } catch (IOException e) {
      log.error("消息回调处理失败!原因: ", e);
      e.printStackTrace();
    }
  }

  public void execute(byte[] payload, MessageCallback callback) {
    executorService.submit(
        () -> {
          try {
            callback.accept(payload);
          } catch (IOException e) {
            log.error("Mqtt回调执行异常!原因: ", e);
            e.printStackTrace();
          }
        });
  }

  public void sendMessage(
      String group, String sn, String mac, MessagePayload payload, MessageCallback callback) {
    try {
      // 注册回调
      cache.put(payload.getToken(), callback);
      // 消息发布
      var sendTopic = properties.getSendTopic();
      var topic = UriComponentsBuilder.fromPath(sendTopic).buildAndExpand(group).toUriString();
      topic = topic.replace("/+/+","")+"/"+sn+"/"+mac;
      client.publish(topic, new MqttMessage(new ObjectMapper().writeValueAsBytes(payload)));
    } catch (MqttException | JsonProcessingException e) {
      log.error("消息发送失败! 原因: ", e);
      e.printStackTrace();
    }
  }
}
