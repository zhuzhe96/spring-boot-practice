package com.zhuzhe.integrationmqtt.mqtt.consumer;

import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttConsumerHandler;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@MqttConsumerHandler
@SuppressWarnings("unused")
public class TestConsumer {
  @MqttSubscribe(topic = "/public/device")
  public void messageAccept(String topic, MqttMessage message) {
    log.info("MQTT 消息回调! topic={}, message={}", topic, message.toString());
  }
}
