package com.zhuzhe.integrationmqtt.mqtt.config;

import com.zhuzhe.integrationmqtt.mqtt.handler.MqttMessageDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.handler.MultiHandlerDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.subscribe.MqttAutoSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.utils.SslUtil;
import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*MQTT配置,启动后连接,连接成功后自动订阅主题*/
@Slf4j
@Configuration
@ConditionalOnProperty(value = "spring.mqtt.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {

  // 构建连接配置对象
  @Bean
  public MqttConnectOptions mqttConnectOptions(MqttProperties properties) {
    return getMqttConnectOptions(properties);
  }

  private MqttConnectOptions getMqttConnectOptions(MqttProperties properties) {
    var options = new MqttConnectOptions();
    options.setUserName(properties.getUsername());
    options.setServerURIs(new String[] {properties.getUrl()});
    options.setPassword(properties.getPassword().toCharArray());
    options.setCleanSession(true);
    options.setKeepAliveInterval(90);
    options.setAutomaticReconnect(true);
    options.setMaxInflight(10000);
    options.setConnectionTimeout(120);
    if (null != properties.getSsl()) {
      options.setSocketFactory(SslUtil.getSslSocket(properties.getSsl()));
    }
    return options;
  }

  // 构建客户端进行连接
  @Bean
  public MqttAsyncClient mqttAsyncClient(MqttProperties properties, MqttConnectOptions options) {
    MqttAsyncClient sampleClient = null;

    try {
      sampleClient = new MqttAsyncClient(properties.getUrl(), properties.getClientId(), null);
      sampleClient.connect(options);
      log.info("MQTT 开始连接...");
      // 如果没有连接上,就每隔10ms重试,直到达到超时限制
      var connected = sampleClient.isConnected();
      var startTime = Clock.systemDefaultZone().millis();
      var timeout = properties.getTimeout() * 1000;
      var endTime = startTime;
      while (!connected && (endTime - startTime) <= timeout) {
        Thread.sleep(10);
        connected = sampleClient.isConnected();
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
    return sampleClient;
  }

  // 构建具体回调派遣者
  @Bean
  public MqttMessageDispatchHandler mqttMessageDispatchHandler() {
    return new MultiHandlerDispatchHandler();
  }

  // 自动给客户端订阅主题,和接收派遣者控制
  @Bean
  @ConditionalOnMissingBean(MqttAutoSubscribe.class)
  public MqttAutoSubscribe mqttAutoSubscribe(
      MqttProperties properties, MqttAsyncClient client, MqttMessageDispatchHandler handler) {
    return new MqttAutoSubscribe(properties, client, handler);
  }
}
