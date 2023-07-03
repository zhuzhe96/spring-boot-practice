package com.zhuzhe.integrationmqtt.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
  // 连接代理服务器地址
  private String url;
  // 连接用户名
  private String username;
  // 连接密码
  private String password;
  // 连接客户端名称
  private String clientId;
  // 超时时间
  private int timeout;
  // 心跳时间
  private int keepalive;

  // 发送主题
  private String sendTopic;
  // 回调主题
  private String handleTopic;
  // 产品分组
  private String group;
}
