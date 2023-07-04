package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import java.time.Clock;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

// 云端请求设备端的消息
@Data
@EqualsAndHashCode(callSuper = true)
public class GetNetworkPayload extends MessagePayload {
  private String wifiName;

  public GetNetworkPayload(String mac, String wifiName) {
    super(
        mac,
        "GET",
        PayloadUrlStorage.WATCH_NETWORK,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null,
        UUID.randomUUID().toString().replace("-", ""));
    this.wifiName = wifiName;
  }
}
