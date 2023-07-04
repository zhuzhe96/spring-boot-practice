package com.zhuzhe.integrationmqtt.mqtt.payload;

// Mqtt请求相关信息
public interface PayloadUrlStorage {
  /*通用: 异步回调的超时时间*/
  long TIMEOUT = 15000;

  /*通用: 设备端请求云端常用接口*/
  // 上线
  String ONLINE = "/zhuzhe/prod/online";
  // 下线
  String OFFLINE = "/zhuzhe/prod/offline";
  // 激活
  String ACTIVE = "/zhuzhe/prod/active";
  // 失活（锁机）
  String INACTIVE = "/zhuzhe/prod/inactive";

  /*手表设备: 云端请求设备端接口*/
  // 网络
  String WATCH_NETWORK = "/zhuzhe/prod/network";
  // 背景图
  String WATCH_BACKGROUND = "/zhuzhe/prod/background";
  // 闹钟
  String WATCH_TIMED_TASK = "/zhuzhe/prod/timed_task";

  /*相机设备: 云端请求设备端接口*/
  String CAMERA_BASE_INFO = "/zhuzhe/prod/base_info";
}
