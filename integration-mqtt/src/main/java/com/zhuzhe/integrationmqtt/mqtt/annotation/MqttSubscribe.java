package com.zhuzhe.integrationmqtt.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

// 发布订阅模式：注解该类指定回调topic中的产品分组
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttSubscribe {
  String value();
}
