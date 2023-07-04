package com.zhuzhe.integrationmqtt.mqtt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.mqtt.MqttDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribeHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

// 发布订阅模式：消息处理者
public class PublishSubscribeDispatchHandler extends MqttDispatchHandler
    implements InitializingBean, ApplicationContextAware {
  private static final Logger log = LoggerFactory.getLogger(PublishSubscribeDispatchHandler.class);
  private ApplicationContext context;

  private record SubscribeHandler(
      Object beanObject, Method handleMethod, String group, String url) {}

  private final List<SubscribeHandler> handlers = new ArrayList<>();

  @Override
  public void setApplicationContext(
      @SuppressWarnings("NullableProblems") ApplicationContext applicationContext)
      throws BeansException {
    this.context = applicationContext;
  }

  // 回调时调用注解方法
  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) {
    try {
      for (SubscribeHandler handler : handlers) {
        String group = handler.group();
        String url = handler.url();
        var payloadHeader =
            new ObjectMapper().readValue(mqttMessage.getPayload(), MessagePayload.class);
        if (topic.contains(group) && url.equals(payloadHeader.getUrl())) {
          try {
            handler.handleMethod().invoke(handler.beanObject(), mqttMessage);
          } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("回调处理异常！原因: ", e);
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // 扫描收集回调方法
  @Override
  public void afterPropertiesSet() {
    var beans = context.getBeansWithAnnotation(MqttSubscribe.class);
    if (beans.size() == 0) {
      throw new RuntimeException("当前应用中没有消息处理对象!");
    }

    beans.forEach(
        (beanName, beanObj) -> {
          Class<?> clazz = beanObj.getClass();

          var mqttSubscribe = AnnotationUtils.findAnnotation(clazz, MqttSubscribe.class);
          if (mqttSubscribe == null || mqttSubscribe.value() == null) {
            throw new RuntimeException("消息处理对象配置错误!");
          }

          var methods = clazz.getMethods();
          if (methods.length == 0) {
            throw new RuntimeException("消息处理对象没有提供处理方法!");
          }

          for (Method method : methods) {
            MqttSubscribeHandler mqttSubscribeHandler =
                AnnotationUtils.findAnnotation(method, MqttSubscribeHandler.class);
            if (mqttSubscribeHandler != null) {
              var group = mqttSubscribe.value();
              var url = mqttSubscribeHandler.value();
              Objects.requireNonNull(url, "回调方法没有提供具体url！");
              var handler = new SubscribeHandler(beanObj, method, group, url);
              handlers.add(handler);
            }
          }
        });
  }
}
