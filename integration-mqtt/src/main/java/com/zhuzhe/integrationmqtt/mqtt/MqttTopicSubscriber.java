package com.zhuzhe.integrationmqtt.mqtt;

import com.zhuzhe.integrationmqtt.mqtt.config.MqttProperties;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.util.UriComponentsBuilder;

// 主题订阅者
public class MqttTopicSubscriber implements InitializingBean {
  private static final Logger log = LoggerFactory.getLogger(MqttTopicSubscriber.class);
  public final MqttProperties properties;
  public final MqttAsyncClient client;
  public final MqttDispatchHandler handler;

  public MqttTopicSubscriber(
      MqttProperties properties, MqttAsyncClient client, MqttDispatchHandler handler) {
    this.properties = properties;
    this.client = client;
    this.handler = handler;
  }

  // 主题订阅
  @Override
  public void afterPropertiesSet() {

    if (Thread.interrupted()) {
      log.info("MQTT 建立连接失败！无法进行主题订阅！");
      return;
    }

    var handleTopic = properties.getHandleTopic();
    var groups = properties.getGroup().split(",");

    var topics =
        Arrays.stream(groups)
            .map(
                group ->
                    UriComponentsBuilder.fromPath(handleTopic).buildAndExpand(group).toUriString())
            .toArray(String[]::new);

    try {
      // 给处理器提供主题订阅信息和客户端
      handler.setMqttTopicSubscriber(this);
      handler.setMqttAsyncClient(client);
      // 给客户端设置回调处理器
      client.setCallback(handler);
      client.subscribe(topics, new int[topics.length]);
      log.info("MQTT 成功订阅主题: {}", Arrays.toString(topics));
    } catch (MqttException e) {
      log.error("MQTT 订阅主题失败! 原因: ", e);
    }
  }
}
