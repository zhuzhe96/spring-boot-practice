package com.zhuzhe.integrationmqtt.entity;

// 实体类：网络
public record Network(
    String wifiName,
    String wifiPassword,
    String cellularType,
    String accessPointName,
    String vpnConfig) {}