package com.zhuzhe.integrationmqtt.mqtt.payload.camera;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuzhe.integrationmqtt.entity.Camera;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GetCameraAckPayload extends MessagePayload {
  private Camera data;

  public GetCameraAckPayload(
      @JsonProperty("id") String id,
      @JsonProperty("type") String type,
      @JsonProperty("url") String url,
      @JsonProperty("timestamp") long timestamp,
      @JsonProperty("status") int status,
      @JsonProperty("message") String message,
      @JsonProperty("token") String token,
      @JsonProperty("data") Camera data) {
    super(id, type, url, timestamp, status, message, token);
    this.data = data;
  }
}
