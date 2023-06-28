package com.zhuzhe.integrationmqtt.mqtt.handler;

import com.zhuzhe.integrationmqtt.mqtt.subscribe.AbstractMqttCallback;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// 抽象调度处理器(持有Spring容器)
public abstract class MqttMessageDispatchHandler extends AbstractMqttCallback implements
    ApplicationContextAware {
  protected ApplicationContext context;

  @Override
  public void setApplicationContext(@SuppressWarnings("NullableProblems") ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
