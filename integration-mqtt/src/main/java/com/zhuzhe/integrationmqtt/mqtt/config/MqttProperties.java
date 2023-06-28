package com.zhuzhe.integrationmqtt.mqtt.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
  /*连接代理服务器地址*/
  private String url;
  /*连接用户名*/
  private String username;
  /*连接密码*/
  private String password;
  /*连接客户端名称*/
  private String clientId;
  /*所属组*/
  private String group;
  /*长连接*/
  private int keepalive;
  /*SSL安全连接秘钥*/
  private String ssl;
  /*超时时间*/
  private int timeout;
  /*订阅主题*/
  private List<MqttTopic> topics = new ArrayList<>();
  @Data
  public static class MqttTopic {
    private String topic;
    private int qos;
  }
}
