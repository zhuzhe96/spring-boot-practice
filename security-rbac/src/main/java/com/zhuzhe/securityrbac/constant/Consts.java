package com.zhuzhe.securityrbac.constant;

public interface Consts {
  // JWT请求头
  String AUTHORIZATION_HEAD = "Authorization";
  String JWT_AUTHORITIES = "Authorities";
  String JWT_SESSION_KEY = "sessionKey";

  String REDIS_JWT_PREFIX = "redis:jwt";
}
