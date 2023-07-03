package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadHeader;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import java.time.Clock;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SetNetworkPayload extends PayloadHeader{
  private Network data;

  public SetNetworkPayload(String id, Network data) {
    super(
        id,
        "POST",
        PayloadUrlStorage.NETWORK,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null);
    this.data = data;
  }
}
