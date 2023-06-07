package com.zhuzhe.securityrbac.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtil {
  public static ServletRequestAttributes getRequestAttributes(){
    var requestAttributes = RequestContextHolder.getRequestAttributes();
    return (ServletRequestAttributes) requestAttributes;
  }

  public static HttpServletRequest getRequest(){
    return getRequestAttributes().getRequest();
  }

  public static HttpServletResponse getResponse(){
    return getRequestAttributes().getResponse();
  }

  public static String getParameter(String name){
    return getRequest().getParameter(name);
  }

  public static HttpSession getSession(){
    return getRequest().getSession();
  }
}
