package com.zhuzhe.integrationmqtt.entity.dto;

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
