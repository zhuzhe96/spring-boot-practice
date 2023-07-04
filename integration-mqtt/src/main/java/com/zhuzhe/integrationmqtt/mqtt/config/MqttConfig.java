package com.zhuzhe.integrationmqtt.mqtt.config;

import com.zhuzhe.integrationmqtt.mqtt.MqttDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.MqttTopicSubscriber;
import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

  // 连接配置
  @Bean
  public MqttConnectOptions mqttConnectOptions(MqttProperties properties) {
    return getMqttConnectOptions(properties);
  }

  private MqttConnectOptions getMqttConnectOptions(MqttProperties properties) {
    log.info("连接配置：{}",properties);
    // 配置连接服务器信息
    var options = new MqttConnectOptions();
    options.setServerURIs(new String[] {properties.getUrl()});
    options.setUserName(properties.getUsername());
    options.setPassword(properties.getPassword().toCharArray());
    // 每次连接新的会话，不处理历史积压消息
    options.setCleanSession(true);
    options.setKeepAliveInterval(properties.getKeepalive());
    // 开启自动重新连接
    options.setAutomaticReconnect(true);
    options.setMaxInflight(10000);
    options.setConnectionTimeout(properties.getTimeout());
    return options;
  }

  // 客户端
  @Bean
  public MqttAsyncClient mqttAsyncClient(MqttProperties properties, MqttConnectOptions options) {
    MqttAsyncClient client = null;

    try {
      client = new MqttAsyncClient(properties.getUrl(), properties.getClientId(), null);
      client.connect(options);
      log.info("MQTT 开始连接...");
      var connected = client.isConnected();
      var startTime = Clock.systemDefaultZone().millis();
      var timeout = properties.getTimeout() * 1000;
      var endTime = startTime;
      while (!connected && (endTime - startTime) <= timeout) {
        Thread.sleep(100);
        connected = client.isConnected();
        endTime = Clock.systemDefaultZone().millis();
      }
      if (!connected) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("MQTT 连接超时, 线程中断!");
      }
      log.info("MQTT 连接成功! 连接地址={}, 连接客户端={}", properties.getUrl(), properties.getClientId());
    } catch (MqttException | InterruptedException e) {
      log.info("MQTT 连接失败! 连接地址={}, 连接客户端={}", properties.getUrl(), properties.getClientId());
    }
    return client;
  }

  // 消息处理者：发布订阅模式
//  @Bean
//  public MqttDispatchHandler mqttDispatchHandler(MqttProperties properties) {
//    //return new PublishSubscribeDispatchHandler();
//  }
  
  
  // 主题订阅
  @Bean
  @ConditionalOnMissingBean(MqttTopicSubscriber.class)
  public MqttTopicSubscriber mqttAutoSubscriber(
      MqttProperties properties, MqttAsyncClient client, MqttDispatchHandler handler) {
    return new MqttTopicSubscriber(properties, client, handler);
  }
}
