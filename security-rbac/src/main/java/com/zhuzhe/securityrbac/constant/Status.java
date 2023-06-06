package com.zhuzhe.securityrbac.constant;

import lombok.Getter;

@Getter
public enum Status {
  SUCCESS(200, "操作成功"),
  ERROR(500, "操作异常"),
  TOKEN_EXPIRED(5002, "token 已过期，请重新登录！"),
  TOKEN_PARSE_ERROR(5002, "token 解析失败，请尝试重新登录！"),
  TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录，请尝试更改密码或重新登录！");

  /*状态码*/
  private final Integer code;
  /*返回信息*/
  private final String message;

  Status(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  // 使用code获取对象
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
