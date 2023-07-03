package com.zhuzhe.integrationmqtt.mqtt.handler.callback;

import org.eclipse.paho.client.mqttv3.MqttMessage;

// 自定义消息处理者, 包括消息的发送和回调处理,消息的监听和回复
interface DeviceMessageDispatcher {
  // 云端 -> 设备端 发送并接收
  void sendMessage(String mac, MqttMessage message, MessageCallback callback);
  // 设备端 -> 云端 监听并回复
  void handleMessage(String mac, MqttMessage message);
}
