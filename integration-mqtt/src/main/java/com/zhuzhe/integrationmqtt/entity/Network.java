package com.zhuzhe.integrationmqtt.entity;

import lombok.Data;

@Data
public class Network {
  private String wifiName;
  private String wifiPassword;
  private String cellularType;
  private String accessPointName;
  private String vpnConfig;
}