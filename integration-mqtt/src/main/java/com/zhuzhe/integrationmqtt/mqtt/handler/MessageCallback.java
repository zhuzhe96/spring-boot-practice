package com.zhuzhe.integrationmqtt.mqtt.handler;

import java.io.IOException;

// 函数式接口:消息回调
public interface MessageCallback {
  void accept(byte[] data) throws IOException;
}
