package com.zhuzhe.integrationmqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface MqttMessageCallback extends MqttCallback {
  // 设置mqtt消费者重连后重新订阅
  void setMqttTopicSubscriber(MqttTopicSubscriber subscriber);
  // 设置客户端
  void setMqttAsyncClient(MqttAsyncClient client);
}
