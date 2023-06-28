package com.zhuzhe.integrationmqtt.mqtt.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevicePayload {
  private int code;
  private String message;
}
