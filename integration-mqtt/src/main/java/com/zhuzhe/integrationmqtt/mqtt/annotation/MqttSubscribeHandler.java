package com.zhuzhe.integrationmqtt.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 发布订阅模式：注解该类指定回调url
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttSubscribeHandler {
  String value();
}
