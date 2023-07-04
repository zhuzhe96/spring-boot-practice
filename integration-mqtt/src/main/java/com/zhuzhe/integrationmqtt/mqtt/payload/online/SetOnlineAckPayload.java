package com.zhuzhe.integrationmqtt.mqtt.payload.online;

import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import java.time.Clock;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// 云端回复设备端的内容
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetOnlineAckPayload extends MessagePayload {
  private OnlineParam data;

  public SetOnlineAckPayload(String id, String token, OnlineParam data) {
    super(
        id,
        "ACK",
        PayloadUrlStorage.ONLINE,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null,
        token);
    this.data = data;
  }

  public record OnlineParam(String result) {}
}
