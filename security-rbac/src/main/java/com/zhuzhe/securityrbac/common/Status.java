package com.zhuzhe.securityrbac.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  SUCCESS(200, "操作成功!"),
  ERROR(500, "操作异常!"),
  LOGOUT(200, "退出异常!"),
  BAD_REQUEST(400, "请求异常!"),
  PARAM_NOT_MATCH(400, "参数有误!"),
  SC_UNAUTHORIZED(401, "令牌过期!"),
  SC_ACCESS_DENIED(403, "访问未授权!"),
  USER_DISABLE(403, "用户已锁定,不能操作!"),
  REQUEST_NOT_FOUND(404, "请求不存在!"),
  REQUEST_NOT_MATCH(405, "请求方式不支持!"),
  SERVER_ERROR(500, "服务器异常, 访问失败!"),
  USERNAME_NOT_FOUND(5001, "用户不存在!"),
  USERNAME_PASSWORD_ERROR(5001, "用户名或密码错误!"),
  USERNAME_EXIST(5002, "用户已存在, 请勿重复创建!"),
  NOT_LOGIN(5002, "用户未登陆!"),
  TOKEN_EXPIRED(5002, "token已过期, 请重新登录!"),
  TOKEN_PARSE_ERROR(5002, "token解析失败, 请尝试重新登录!"),
  TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录, 请尝试更改密码或重新登录!"),
  KICK_OUT_SELF(5004, "无法手动踢出自己, 请尝试退出登陆操作!");


  private final Integer code;
  private String message;

  public Status custStatusMsg(String message) {
    this.message = message;
    return this;
  }

  // 使用code获取对象
  @SuppressWarnings("unused")
  public static Status formCode(Integer code) {
    var statuses = Status.values();
    for (Status status : statuses) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    return SUCCESS;
  }

  @Override
  public String toString() {
    return String.format("Status:{code=%s, message=%s}", getCode(), getMessage());
  }
}
