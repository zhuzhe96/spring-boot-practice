package com.zhuzhe.integrationmqtt.mqtt.payload;

// Mqtt请求相关信息
public interface PayloadUrlStorage {
  // 网络
  String NETWORK = "/zhuzhe/prod/network";
  // 背景图
  String BACKGROUND = "/zhuzhe/prod/background";
  // 闹钟
  String TIMED_TASK = "/zhuzhe/prod/timed_task";
}
