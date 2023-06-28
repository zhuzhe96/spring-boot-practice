package com.zhuzhe.integrationmqtt.receiver;

import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttConsumerHandler;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@MqttConsumerHandler
@SuppressWarnings("unused")
public class DeviceReceiver {
  @MqttSubscribe(topic = "/public/cloud/M1313")
  public void acceptM1313(String topic, MqttMessage message) {
    log.info("MQTT 消息回调! topic={}, message={}", topic, message.toString());
  }

  @MqttSubscribe(topic = "/public/cloud/L3DN-1A")
  public void acceptL3DN1A(String topic, MqttMessage message) {
    log.info("MQTT 消息回调! topic={}, message={}", topic, message.toString());
  }
}
