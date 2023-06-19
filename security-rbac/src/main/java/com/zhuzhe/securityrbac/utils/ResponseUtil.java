package com.zhuzhe.securityrbac.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResponseUtil {
  @Autowired private ObjectMapper objectMapper;

  public void setCommonHead(HttpServletResponse response, HttpStatus httpStatus){
    response.setHeader("Access-Control-Allow-Origin","*");
    response.setHeader("Access-Control-Allow-Methods","*");
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(httpStatus.value());
  }

  public void renderJson(HttpServletResponse response, HttpStatus httpStatus, ApiResponse<?> apiResponse){
    setCommonHead(response, httpStatus);
    try {
      objectMapper.writeValue(response.getWriter(), apiResponse);
    } catch (IOException e) {
      log.error("渲染success返回失败, 原因: {}",e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void renderJson(HttpServletResponse response, HttpStatus httpStatus, BaseException exception){
    setCommonHead(response, httpStatus);
    try {
      objectMapper.writeValue(response.getWriter(), exception);
    } catch (IOException e) {
      log.info("渲染exception返回失败, 原因: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
