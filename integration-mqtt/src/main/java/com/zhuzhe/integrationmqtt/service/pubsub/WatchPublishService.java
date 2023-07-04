package com.zhuzhe.integrationmqtt.service.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.SetNetworkPayload;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 发布订阅模式：发布者
@Slf4j
@Service
public class WatchPublishService {
  @Autowired private MqttAsyncClient client;

  public void setNetWork(String sn, String mac, Network network) {
    try {
      var setNetworkPayload = new SetNetworkPayload(mac, network);
      client.publish(
          "/test/device/watch/%s/%s".formatted(sn, mac),
          new MqttMessage(new ObjectMapper().writeValueAsBytes(setNetworkPayload)));
    } catch (MqttException | JsonProcessingException e) {
      log.error("消息发布失败! 原因: ", e);
    }
    log.info("成功发送消息!");
  }
}
