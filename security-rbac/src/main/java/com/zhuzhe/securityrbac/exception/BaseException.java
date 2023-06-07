package com.zhuzhe.securityrbac.exception;

import com.zhuzhe.securityrbac.common.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException{
  private Integer code;
  private String message;
  private Object data;

  public BaseException(Integer code, String message){
    this.code = code;
    this.message = message;
  }

  public BaseException(Status status){
    this(status.getCode(),status.getMessage());
  }

  public BaseException(Status status, Object data){
    this(status);
    this.data = data;
  }
}
