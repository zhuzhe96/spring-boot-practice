package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GetNetworkAckPayload extends MessagePayload {
  private Network data;

  @JsonCreator
  public GetNetworkAckPayload(
      @JsonProperty("id") String id,
      @JsonProperty("type") String type,
      @JsonProperty("url") String url,
      @JsonProperty("timestamp") long timestamp,
      @JsonProperty("status") int status,
      @JsonProperty("message") String message,
      @JsonProperty("token") String token,
      @JsonProperty("data") Network data) {
    super(id, type, url, timestamp, status, message, token);
    this.data = data;
  }
}
