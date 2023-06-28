package com.zhuzhe.integrationmqtt.mqtt.handler;

import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttConsumerHandler;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

// 具体的调度处理器
public class MultiHandlerDispatchHandler extends MqttMessageDispatchHandler
    implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(MultiHandlerDispatchHandler.class);
  private final List<ConsumerHandler> consumerHandlers = new ArrayList<>();

  // 消息回复时,找到回调方法并调用
  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) {
    consumerHandlers.forEach(
        handler -> {
          String pattern = handler.pattern();
          boolean matches = Pattern.matches(pattern, topic);
          if (matches) {
            try {
              handler.handleMethod().invoke(handler.beanObject(), topic, mqttMessage);
              log.info("来自topic=%s的数据，已被handler=%s的回调进行处理.".formatted(topic, handler.beanName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
              log.error("回调处理异常！原因: ", e);
            }
          }
        });
  }

  // 启动时将扫描添加所有注解的回调方法到map中
  @Override
  public void afterPropertiesSet() {
    // 获取添加了消费者注解的类
    var handlerArray = context.getBeansWithAnnotation(MqttConsumerHandler.class);
    if (handlerArray.size() == 0) {
      throw new RuntimeException("当前Spring容器中没有提供消费者！");
    }

    handlerArray.forEach(
        (beanName, beanObj) -> {
          Class<?> clazz = beanObj.getClass();
          var methods = clazz.getMethods();
          if (methods.length == 0) {
            throw new RuntimeException("使用@MqttConsumerHandler注解的类中必须提供回调方法！");
          }
          // 一个类中只找出一个回调方法
          Method handlerMethod = null;
          for (Method method : methods) {
            var annotation = method.getAnnotation(MqttSubscribe.class);
            if (annotation != null) {
              handlerMethod = method;
              break;
            }
          }
          // 将回调方法存储到map中
          Objects.requireNonNull(handlerMethod, "当前类中没有回调方法");
          MqttSubscribe mqttSubscribe = AnnotationUtils.getAnnotation(handlerMethod, MqttSubscribe.class);
          Objects.requireNonNull(mqttSubscribe, "当前回调方法的配置为空！");
          String pattern = mqttSubscribe.topic();
          var consumerHandler = new ConsumerHandler(beanName, beanObj, handlerMethod, pattern);
          consumerHandlers.add(consumerHandler);
        });
  }

  private record ConsumerHandler(
      Object beanName, Object beanObject, Method handleMethod, String pattern) {}
}
