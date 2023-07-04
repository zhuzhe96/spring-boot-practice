package com.zhuzhe.integrationmqtt.service.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribeHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.SetNetworkAckPayload;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

// 发布订阅模式：订阅者
@Slf4j
@Component
@MqttSubscribe("watch")
@SuppressWarnings("unused")
public class WatchSubscribeService {
  @MqttSubscribeHandler(PayloadUrlStorage.NETWORK)
  public void setNetWorkCallback(MqttMessage mqttMessage) throws IOException {
    var setNetworkAckPayload = new ObjectMapper().readValue(mqttMessage.getPayload(),
        SetNetworkAckPayload.class);
    log.info("设置网络结果: {}",setNetworkAckPayload);
  }
}
