package com.zhuzhe.integrationmqtt.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/*注解类，表明是消费者类*/
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttConsumerHandler {
  @AliasFor(annotation = Component.class)
  String value() default "";
}
