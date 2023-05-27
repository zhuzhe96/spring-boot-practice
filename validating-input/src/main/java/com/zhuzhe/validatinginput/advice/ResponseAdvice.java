package com.zhuzhe.validatinginput.advice;

import com.zhuzhe.common.entity.ResultData;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/*当前controller的返回封装*/
@RestControllerAdvice(basePackages = "com.zhuzhe.validatinginput.controller")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  @SneakyThrows
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {
    // 防止重复封装
    if(body instanceof ResultData){
      return ResponseEntity.ok(body);
    }
    return ResultData.success(body);
  }
}
