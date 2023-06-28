package com.zhuzhe.integrationmqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.mqtt.payload.DevicePayload;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientService {

  @Autowired
  private MqttAsyncClient mqttAsyncClient;

  public String sendMqttMessage(){
    var payload = new DevicePayload(0, "client-send-message");
    try {
      mqttAsyncClient.publish("/public/device/M1313", new MqttMessage(new ObjectMapper().writeValueAsBytes(payload)));
      log.info("成功发布消息");
    } catch (MqttException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return "success";
  }
}
