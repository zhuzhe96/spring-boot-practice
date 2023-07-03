package com.zhuzhe.integrationmqtt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribeHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import com.zhuzhe.integrationmqtt.mqtt.payload.network.SetNetworkAckPayload;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * 使用传统发布订阅方式处理设备信息, 常用于设置操作,并且这里也无法判断是get的回复还是set的回复
 * 1. 发布与订阅方法分离,无法做到异步回调前端数据
 * 2. 并且定义复杂, 像这里我的回调只适合设置数据的操作
 * 3. 存在回调内容我无法判断是get返回还是set返回,除非我新增字段或者在url上做区分
 */
@Slf4j
@Component
@MqttSubscribe("watch")
@SuppressWarnings("unused")
public class WatchSubscribeService {
  @MqttSubscribeHandler(PayloadUrlStorage.NETWORK)
  public void setNetWorkCallback(MqttMessage mqttMessage) throws IOException {
    var setNetworkAckPayload = new ObjectMapper().readValue(mqttMessage.getPayload(),
        SetNetworkAckPayload.class);
    log.info("设置网络结果: {}",setNetworkAckPayload);
  }
}
