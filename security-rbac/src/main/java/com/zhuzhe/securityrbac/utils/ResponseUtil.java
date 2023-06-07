package com.zhuzhe.securityrbac.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.securityrbac.common.Status;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtil {

  record SuccessResponse(String token, Object obj){}
  public static void renderJson(HttpServletResponse response, Status status, Object data){
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods","*");
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(200);
    try {
      var writer = response.getWriter();
      var objectMapper = new ObjectMapper();
      //objectMapper.writeValue(response.getWriter(), new SuccessResponse());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
