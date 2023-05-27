package com.zhuzhe.common.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DataStatus {
  /**操作成功**/
  SUCCESS(1000,"操作成功"),
  /**无权访问**/
  NO_PERMISSION(1001,"无访问权限,请联系管理员授予权限"),
  /**匿名访问资源失败**/
  ACCESS_FAILED(1002,"匿名用户访问无权限资源时的异常"),
  /**服务异常**/
  FAIL(1003,"系统异常，请稍后重试"),
  INVALID_TOKEN(1004,"访问令牌不合法"),
  ACCESS_DENIED(1005,"没有权限访问该资源"),
  CLIENT_AUTHENTICATION_FAILED(1006,"客户端认证失败"),
  USERNAME_OR_PASSWORD_ERROR(1007,"用户名或密码错误"),
  UNSUPPORTED_GRANT_TYPE(1008, "不支持的认证模式"),
  INVALID_INPUT(1009, "无效输入");
  /**自定义状态码**/
  private final int code;
  /**自定义描述**/
  private final String message;
  public int getCode() {
    return code;
  }
  public String getMessage() {
    return message;
  }
}
