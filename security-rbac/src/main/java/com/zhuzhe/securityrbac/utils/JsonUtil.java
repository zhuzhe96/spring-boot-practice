package com.zhuzhe.securityrbac.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("all")
public class JsonUtil {
  @Autowired private ObjectMapper objectMapper;

  /*将json字符串转为指定类型对象*/
  public <T> T toObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("ObjectMapper读取Json失败! {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /*将json字符串转为指定泛型对象*/
  public <T> T toObject(String json, TypeReference<T> valueTypeRef) {
    try {
      return objectMapper.readValue(json, valueTypeRef);
    } catch (JsonProcessingException e) {
      log.error("ObjectMapper读取Json失败!{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /*将json字符串转为指定Map对象*/
  public <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
    try {
      var mapType = objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
      return objectMapper.readValue(json, mapType);
    } catch (JsonProcessingException e) {
      log.error("ObjectMapper读取Json失败!{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /*将json字符串转为JsonNode对象*/
  public JsonNode toTree(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (JsonProcessingException e) {
      log.error("ObjectMapper读取Json失败!{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /*将对象转为json字符串*/
  public String toJson(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("ObjectMMapper输出Json失败!{}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void writeResponse(HttpServletResponse response, Object obj){

  }
}
