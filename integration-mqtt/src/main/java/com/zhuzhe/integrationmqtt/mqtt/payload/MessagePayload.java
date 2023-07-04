package com.zhuzhe.integrationmqtt.mqtt.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 公用荷载
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagePayload {
  // 设备id
  private String id;
  // 请求类型
  private String type;
  // 请求地址
  private String url;
  // 消息时间戳
  private long timestamp;
  // 消息状态: 200-正常, 400-异常, 401-未授权, 404-找不到, 500-设备端错误
  private int status;
  // 消息信息: 当发生异常时,将从这里提示异常信息
  private String message;
  // token，用来识别一对发送和回复的消息
  private String token;
}
