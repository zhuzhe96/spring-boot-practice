package com.zhuzhe.integrationmqtt.mqtt.subscribe;

import com.zhuzhe.integrationmqtt.mqtt.config.MqttProperties;
import com.zhuzhe.integrationmqtt.mqtt.handler.MqttMessageDispatchHandler;
import io.micrometer.common.util.StringUtils;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

// 自动主题订阅处理器
public class MqttAutoSubscribe implements InitializingBean {
  private static final Logger log = LoggerFactory.getLogger(MqttAutoSubscribe.class);
  // 带组名的主题前缀
  public static final String GROUP_SUBSCRIBE_PREFIX = "/group";
  // 默认的主题前缀
  public static final String DEFAULT_SUBSCRIBE_PREFIX = "/public";

  // 连接配置
  public final MqttProperties mqttProperties;
  // 客户端
  public final MqttAsyncClient mqttAsyncClient;
  // 回调调度处理器
  public final MqttMessageDispatchHandler dispatchHandler;

  public MqttAutoSubscribe(
      MqttProperties mqttProperties,
      MqttAsyncClient mqttAsyncClient,
      MqttMessageDispatchHandler dispatchHandler) {
    this.mqttProperties = mqttProperties;
    this.mqttAsyncClient = mqttAsyncClient;
    this.dispatchHandler = dispatchHandler;
  }

  // 自动主题订阅
  @Override
  public void afterPropertiesSet() {
    // 检查当前线程是否中断
    if (Thread.interrupted()) {
      return;
    }

    var mqttTopics = mqttProperties.getTopics();
    if (CollectionUtils.isEmpty(mqttTopics)) {
      log.warn("配置文件中没有提供主题!");
      return;
    }

    var topics =
        mqttTopics.stream()
            .map(
                mqttTopic -> {
                  var group = mqttProperties.getGroup();
                  var topic = mqttTopic.getTopic();
                  if (!StringUtils.isEmpty(group)) {
                    return GROUP_SUBSCRIBE_PREFIX + "/" + group + "/" + topic;
                  } else {
                    return DEFAULT_SUBSCRIBE_PREFIX + "/" + topic;
                  }
                })
            .toList()
            .toArray(new String[mqttTopics.size()]);

    int[] qos =
        mqttTopics.stream()
            .map(MqttProperties.MqttTopic::getQos)
            .mapToInt(Integer::intValue)
            .toArray();

    try {
      dispatchHandler.setMqttAutoSubscribe(this);
      dispatchHandler.setMqttAsyncClient(mqttAsyncClient);
      // 设置回调调度处理器和主题订阅
      mqttAsyncClient.setCallback(dispatchHandler);
      mqttAsyncClient.subscribe(topics, qos);
      log.info("MQTT 成功订阅主题: {}", Arrays.toString(topics));
    } catch (MqttException e) {
      log.error("MQTT 订阅主题失败! 原因: ", e);
    }
  }
}
