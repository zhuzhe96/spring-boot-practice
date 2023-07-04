package com.zhuzhe.integrationmqtt.mqtt.payload.camera;

import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.MqttStatus;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import java.time.Clock;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetCameraPayload extends MessagePayload {

  public GetCameraPayload(String id) {
    super(
        id,
        "GET",
        PayloadUrlStorage.CAMERA_BASE_INFO,
        Clock.systemDefaultZone().millis(),
        MqttStatus.SUCCESS.getCode(),
        null,
        UUID.randomUUID().toString().replace("-", ""));
  }
}
