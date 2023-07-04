package com.zhuzhe.integrationmqtt.service.camera;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.integrationmqtt.entity.Camera;
import com.zhuzhe.integrationmqtt.mqtt.handler.AsyncCallbackDispatchHandler;
import com.zhuzhe.integrationmqtt.mqtt.payload.PayloadUrlStorage;
import com.zhuzhe.integrationmqtt.mqtt.payload.camera.GetCameraAckPayload;
import com.zhuzhe.integrationmqtt.mqtt.payload.camera.GetCameraPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@Service
public class CameraService {
  public static final String group = "camera";
  @Autowired
  private AsyncCallbackDispatchHandler handler;

  public DeferredResult<Camera> get(String sn, String mac) {
    var result = new DeferredResult<Camera>(PayloadUrlStorage.TIMEOUT, () -> new RuntimeException("获取相机基础信息超时!"));
    log.info("开始通过MQTT调用设备端...");
    handler.sendMessage(
        group,
        sn,
        mac,
        new GetCameraPayload(mac),
        (bytes) -> {
          log.info("MQTT 回调处理");
          var getCameraAckPayload =
              new ObjectMapper().readValue(bytes, GetCameraAckPayload.class);
          log.info("MQTT 回调处理成功! Payload={}", getCameraAckPayload);
          result.setResult(getCameraAckPayload.getData());
        });
    return result;
  }
}
