package com.zhuzhe.integrationmqtt.mqtt.payload;

// Mqtt请求相关信息
public interface PayloadUrlStorage {
  /*设备端请求云端*/
  // 上线
  String ONLINE = "/zhuzhe/prod/online";
  // 下线
  String OFFLINE = "/zhuzhe/prod/offline";
  // 激活
  String ACTIVE = "/zhuzhe/prod/active";
  // 失活（锁机）
  String INACTIVE = "/zhuzhe/prod/inactive";

  /*云端请求设备端*/
  // 网络
  String NETWORK = "/zhuzhe/prod/network";
  // 背景图
  String BACKGROUND = "/zhuzhe/prod/background";
  // 闹钟
  String TIMED_TASK = "/zhuzhe/prod/timed_task";
}
