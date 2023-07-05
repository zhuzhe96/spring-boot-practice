package com.zhuzhe.integrationmqtt.service.watch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribe;
import com.zhuzhe.integrationmqtt.mqtt.annotation.MqttSubscribeHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import com.zhuzhe.integrationmqtt.mqtt.payload.online.SetOnlineAckPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.online.SetOnlineAckPayload.OnlineParam;
import com.zhuzhe.integrationmqtt.mqtt.payload.online.SetOnlinePayload;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@MqttSubscribe("watch")
@SuppressWarnings("unused")
public class WatchReplyService {
  @Autowired private ObjectMapper objectMapper;

  // 设备端 -> 云端: 接收并回复
  @MqttSubscribeHandler(PayloadUrlStorage.ONLINE)
  public MessagePayload online(byte[] bytes) throws IOException {
    var setOnlinePayload = objectMapper.readValue(bytes, SetOnlinePayload.class);
    log.info("设置在线信息: {}", setOnlinePayload);
    return new SetOnlineAckPayload(
        setOnlinePayload.getId(), setOnlinePayload.getToken(), new OnlineParam("成功上线!"));
  }
}
