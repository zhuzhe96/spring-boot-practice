package com.zhuzhe.integrationmqtt.entity;

public record Network(
    String wifiName,
    String wifiPassword,
    String cellularType,
    String accessPointName,
    String vpnConfig) {}