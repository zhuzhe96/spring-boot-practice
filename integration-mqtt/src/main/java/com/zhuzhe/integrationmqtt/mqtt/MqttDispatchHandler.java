package com.zhuzhe.integrationmqtt.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 回调处理者抽象父类
 * 这里做抽象是为了给子类提供断线重连功能
 */
public abstract class MqttDispatchHandler implements MqttMessageCallback {
  private static final Logger log = LoggerFactory.getLogger(MqttDispatchHandler.class);

  private MqttAsyncClient client;
  private MqttTopicSubscriber subscriber;

  public MqttDispatchHandler() {

  }

  @Override
  public void setMqttTopicSubscriber(MqttTopicSubscriber subscriber) {
    this.subscriber = subscriber;
  }

  @Override
  public void setMqttAsyncClient(MqttAsyncClient client) {
    this.client = client;
  }

  // 连接丢失时进行重试
  @Override
  public void connectionLost(Throwable throwable) {
    log.warn("MQTT 连接丢失! 原因: ", throwable);
    try {
      var connected = client.isConnected();
      if (connected) {
        client.disconnect();
      }
      client.reconnect();
      connected = client.isConnected();
      while (!connected) {
        connected = client.isConnected();
      }
      log.info("MQTT 重连成功! 重新订阅主题!");
      if (null != this.subscriber) {
        subscriber.afterPropertiesSet();
      }
    } catch (Exception e) {
      log.info("MQTT 重连失败! 原因: " + e);
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    log.info("MQTT 消息发送成功!");
  }
}
