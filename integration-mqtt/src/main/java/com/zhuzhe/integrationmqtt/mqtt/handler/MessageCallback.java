package com.zhuzhe.integrationmqtt.mqtt.handler;

import java.io.IOException;

// 异步回调模式：回调接口
public interface MessageCallback {
  void accept(byte[] data) throws IOException;
}
