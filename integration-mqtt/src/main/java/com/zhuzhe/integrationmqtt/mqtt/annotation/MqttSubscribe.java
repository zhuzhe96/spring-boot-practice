package com.zhuzhe.integrationmqtt.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*注解方法，表明是回调方法*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttSubscribe {
  String topic() default "\\.*.*";
  String value() default "";
}
