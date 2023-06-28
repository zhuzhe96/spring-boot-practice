package com.zhuzhe.integrationmqtt.mqtt.subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// MQTT回调抽象类型,并实现基本的重连等方法
public abstract class AbstractMqttCallback implements MqttMessageCallback {
  private static final Logger log = LoggerFactory.getLogger(AbstractMqttCallback.class);

  private MqttAsyncClient mqttAsyncClient;
  private MqttAutoSubscribe mqttAutoSubscribe;

  public AbstractMqttCallback() {}

  @Override
  public void setMqttAutoSubscribe(MqttAutoSubscribe mqttAutoSubscribe) {
    this.mqttAutoSubscribe = mqttAutoSubscribe;
  }

  @Override
  public void setMqttAsyncClient(MqttAsyncClient mqttAsyncClient) {
    this.mqttAsyncClient = mqttAsyncClient;
  }

  // 连接丢失时进行重连
  @Override
  public void connectionLost(Throwable throwable) {
    log.warn("MQTT 连接丢失! 原因: ", throwable);
    try {
      var connected = mqttAsyncClient.isConnected();
      if (connected) {
        mqttAsyncClient.disconnect();
      }
      mqttAsyncClient.reconnect();
      connected = mqttAsyncClient.isConnected();
      while (!connected) {
        log.info("MQTT 重连...");
        connected = mqttAsyncClient.isConnected();
      }
      log.info("MQTT 重连成功! 重新订阅主题!");
      if (null != this.mqttAutoSubscribe) {
        mqttAutoSubscribe.afterPropertiesSet();
      }
    } catch (Exception e) {
      log.info("MQTT 重连失败! 原因: " + e);
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    log.info("MQTT 消息已被成功接收!");
  }
}
