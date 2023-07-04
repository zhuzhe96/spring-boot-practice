package com.zhuzhe.integrationmqtt.mqtt.payload.online;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// 设备端请求云端的内容
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetOnlinePayload extends MessagePayload {
  private OnlineParam data;

  public SetOnlinePayload(
      @JsonProperty("id") String id,
      @JsonProperty("type") String type,
      @JsonProperty("url") String url,
      @JsonProperty("timestamp") long timestamp,
      @JsonProperty("status") int status,
      @JsonProperty("message") String message,
      @JsonProperty("token") String token,
      @JsonProperty("data") OnlineParam data) {
    super(id, type, url, timestamp, status, message, token);
    this.data = data;
  }
  public record OnlineParam(String key) {}
}
