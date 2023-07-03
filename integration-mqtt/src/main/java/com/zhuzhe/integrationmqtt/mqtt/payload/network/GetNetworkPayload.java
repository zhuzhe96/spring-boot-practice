package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadHeader;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import java.time.Clock;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetNetworkPayload extends PayloadHeader {
  private String wifiName;

  public GetNetworkPayload(String id, String wifiName) {
    super(
        id,
        "GET",
        PayloadUrlStorage.NETWORK,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null);
    this.wifiName = wifiName;
  }
}
