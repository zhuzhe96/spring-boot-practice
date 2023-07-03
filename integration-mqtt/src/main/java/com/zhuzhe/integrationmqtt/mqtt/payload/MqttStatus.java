package com.zhuzhe.integrationmqtt.mqtt.payload;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import lombok.Getter;

@Getter
@JsonSerialize(using = MqttStatus.Serializer.class)// 自定义序列化方式
public enum MqttStatus {
  // 成功
  SUCCESS(200, "success"),
  // 失败
  FAILURE(400, "failure"),
  // 未授权
  UNAUTHORIZED(401, "access unauthorized"),
  // 未找到
  NOT_FOUND(404, "requested resource not found"),
  // 服务器错误
  ERROR(500, "server error");

  private final int code;
  private final String message;

  MqttStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  // 自定义序列化
  public static class Serializer extends JsonSerializer<MqttStatus> {
    @Override
    public void serialize(
        MqttStatus mqttStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeFieldName("code");
      jsonGenerator.writeNumber(mqttStatus.getCode());
      jsonGenerator.writeFieldName("message");
      jsonGenerator.writeString(mqttStatus.getMessage());
      jsonGenerator.writeEndObject();
    }
  }
}
