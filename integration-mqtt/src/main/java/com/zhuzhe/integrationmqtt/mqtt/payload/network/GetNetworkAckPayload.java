package com.zhuzhe.integrationmqtt.mqtt.payload.network;


import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetNetworkAckPayload extends PayloadHeader {
  private Network data;
  public GetNetworkAckPayload(String id, String type, String url, long timestamp, int status,
      String message, Network data) {
    super(id, type, url, timestamp, status, message);
    this.data = data;
  }
}
