package com.zhuzhe.integrationmqtt.mqtt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zhuzhe.integrationmqtt.mqtt.MqttDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribeHandler;
import com.zhuzhe.integrationmqtt.mqtt.config.MqttProperties;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 消息处理者
 * 1. 云端 -> 设备端: 调用后使用token将回调函数保存到回调缓存,当设备回复时执行缓存中的回调函数
 * 2. 设备端 -> 云端: 初始化后扫描注解将回复函数保存到回复缓存,当设备调用时执行缓存中的回复函数
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncCallbackDispatchHandler extends MqttDispatchHandler
    implements InitializingBean, ApplicationContextAware {
  // 回调缓存
  private final Cache<String, MessageCallback> callbackCache =
      Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
  // 回复缓存
  private record MessageReply(Object obj, Method method, String group, String url) {}

  private final List<MessageReply> replyCache = new ArrayList<>();

  private final ScheduledExecutorService executorService =
      new ScheduledThreadPoolExecutor(
          8,
          runnable -> {
            var thread = new Thread(runnable);
            thread.setName("message-callback-thread");
            thread.setUncaughtExceptionHandler((t, e) -> log.error("线程执行发生异常! 原因: ", e));
            return thread;
          });

  private ApplicationContext context;
  private final MqttProperties properties;
  private final ObjectMapper objectMapper;

  @Override
  public void setApplicationContext(
      @SuppressWarnings("NullableProblems") ApplicationContext applicationContext)
      throws BeansException {
    this.context = applicationContext;
  }

  // 扫描注解将回复函数添加缓存
  @Override
  public void afterPropertiesSet() {
    var beans = context.getBeansWithAnnotation(MqttSubscribe.class);
    if (beans.size() == 0) {
      throw new RuntimeException("当前应用中没有回复对象!");
    }
    beans.forEach(
        (beanName, beanObj) -> {
          Class<?> clazz = beanObj.getClass();

          var mqttSubscribe = AnnotationUtils.findAnnotation(clazz, MqttSubscribe.class);
          if (mqttSubscribe == null || mqttSubscribe.value() == null) {
            throw new RuntimeException("回复对象配置错误!");
          }

          var methods = clazz.getMethods();
          if (methods.length == 0) {
            throw new RuntimeException("回复对象没有提供方法!");
          }

          for (Method method : methods) {
            MqttSubscribeHandler mqttSubscribeHandler =
                AnnotationUtils.findAnnotation(method, MqttSubscribeHandler.class);
            if (mqttSubscribeHandler != null) {
              var group = mqttSubscribe.value();
              var url = mqttSubscribeHandler.value();
              Objects.requireNonNull(url, "回复方法配置错误！");
              var reply = new MessageReply(beanObj, method, group, url);
              replyCache.add(reply);
            }
          }
        });
  }

  public void executeReply(String handleTopic, byte[] payload, MessageReply reply) {
    executorService.submit(
        () -> {
          try {
            var messagePayload =
                (MessagePayload) reply.method().invoke(reply.obj(), (Object) payload);
            // 消息发布
            var sendTopic = properties.getSendTopic();
            var param = handleTopic.split("/");
            var topic =
                UriComponentsBuilder.fromPath(sendTopic)
                    .buildAndExpand(reply.group())
                    .toUriString();
            topic = topic.replace("/+/+", "") + "/" + param[4] + "/" + param[5];
            client.publish(topic, new MqttMessage(objectMapper.writeValueAsBytes(messagePayload)));
          } catch (Exception e) {
            log.error("MQTT 执行回复异常! 原因: ", e);
            e.printStackTrace();
          }
        });
  }

  // 发送消息并将回调函数添加缓存
  public void sendMessage(
      String group, String sn, String mac, MessagePayload payload, MessageCallback callback) {
    try {
      // 注册回调
      callbackCache.put(payload.getToken(), callback);
      // 消息发布
      var sendTopic = properties.getSendTopic();
      var topic = UriComponentsBuilder.fromPath(sendTopic).buildAndExpand(group).toUriString();
      topic = topic.replace("/+/+", "") + "/" + sn + "/" + mac;
      client.publish(topic, new MqttMessage(objectMapper.writeValueAsBytes(payload)));
    } catch (MqttException | JsonProcessingException e) {
      log.error("消息发送失败! 原因: ", e);
      e.printStackTrace();
    }
  }

  public void executeCallback(byte[] payload, MessageCallback callback) {
    executorService.submit(
        () -> {
          try {
            callback.accept(payload);
          } catch (IOException e) {
            log.error("MQTT 执行回调异常! 原因: ", e);
            e.printStackTrace();
          }
        });
  }

  // mqtt 回复
  @Override
  public void messageArrived(String topic, MqttMessage message) {
    try {
      var payload = message.getPayload();
      var messagePayload = objectMapper.readValue(payload, MessagePayload.class);
      if (StringUtils.isBlank(messagePayload.getUrl()))
        throw new RuntimeException("无效的Mqtt消息, 原因: url为空!");

      switch (messagePayload.getUrl()) {
        case PayloadUrlStorage.ONLINE,
            PayloadUrlStorage.OFFLINE,
            PayloadUrlStorage.ACTIVE,
            PayloadUrlStorage.INACTIVE -> {
          var optional =
              replyCache.stream()
                  .filter(r -> topic.contains(r.group()) && r.url().equals(messagePayload.getUrl()))
                  .findFirst();
          if (optional.isEmpty()) {
            log.info("找不到回复方法!");
            return;
          }
          executeReply(topic, payload, optional.get());
        }
        case PayloadUrlStorage.WATCH_NETWORK,
            PayloadUrlStorage.WATCH_BACKGROUND,
            PayloadUrlStorage.WATCH_TIMED_TASK,
            PayloadUrlStorage.CAMERA_BASE_INFO -> {
          var token = messagePayload.getToken();
          var callback = callbackCache.getIfPresent(token);
          if (callback == null) {
            log.info("找不到回调方法!");
            return;
          }
          callbackCache.invalidate(token);
          executeCallback(payload, callback);
        }
      }
    } catch (IOException e) {
      log.error("消息处理失败! 原因: ", e);
      e.printStackTrace();
    }
  }
}
