package com.zhuzhe.securityrbac.exception;

import com.zhuzhe.securityrbac.common.Status;

public class SecurityException extends BaseException {

  public SecurityException(Integer code, String message, Object data) {
    super(code, message, data);
  }

  public SecurityException(Status status) {
    super(status);
  }

  public SecurityException(Status status, Object data) {
    super(status, data);
  }
}
