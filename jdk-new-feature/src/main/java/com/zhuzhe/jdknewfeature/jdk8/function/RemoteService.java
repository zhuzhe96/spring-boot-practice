package com.zhuzhe.jdknewfeature.jdk8.function;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface RemoteService {
  /**常量*/
  String host = "127.0.0.1";

  /**函数式接口*/
  void callback(byte[] data);

  /**静态方法*/
  static String getHost() {
    return host;
  }

  /**默认方法*/
  default Map<String, String> getDefaultStorage() {
    return new HashMap<>(Map.of("ip","192.168.101.1", "mac", "D0CA88BNT332"));
  }

  /**Object的方法*/
  @Override
  boolean equals(Object obj);
}
