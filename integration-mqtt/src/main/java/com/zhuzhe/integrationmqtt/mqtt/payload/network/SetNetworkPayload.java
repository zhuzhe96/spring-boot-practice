package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.zhuzhe.integrationmqtt.entity.Network;
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
public class SetNetworkPayload extends MessagePayload {
  private Network data;

  public SetNetworkPayload(String id, Network data) {
    super(
        id,
        "POST",
        PayloadUrlStorage.WATCH_NETWORK,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null,
        UUID.randomUUID().toString().replace("-", ""));
    this.data = data;
  }
}
