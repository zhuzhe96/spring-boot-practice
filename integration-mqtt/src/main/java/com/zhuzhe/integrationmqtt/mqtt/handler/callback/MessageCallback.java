package com.zhuzhe.integrationmqtt.mqtt.handler.callback;

// 函数式编程: 回调接口
public interface MessageCallback {
  void accept(byte[] data);
}
