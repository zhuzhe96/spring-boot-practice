package com.zhuzhe.securityrbac.common;

public interface Consts {

  /*Redis相关*/
  String REDIS_JWT_PREFIX = "redis:jwt:";
  String REDIS_USER_PREFIX = "redis:user:";

  /*Security & JWT相关*/
  String AUTHORIZATION_HEAD = "Authorization";
  String JWT_AUTHORITIES = "Authorities";
  String JWT_SESSION_KEY = "sessionKey";

  /*用户id的JWT加密使用*/
  String JWT_CLAIM_KEY = "login_user_id";

  /*用户状态*/
  Integer USER_DELETE = -1;
  Integer USER_DISABLE = 0;

  /*菜单相关*/
  String MENU = "M";
  String BTN = "B";
}
