package com.zhuzhe.securityrbac.common;

import com.zhuzhe.securityrbac.exception.BaseException;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
  private Integer code;
  private String message;
  private T data;

  public static <T> ApiResponse<T> of(Integer code, String message, T data){
    return new ApiResponse<>(code, message, data);
  }

  public static ApiResponse<?> ofSuccess(){
    return ofSuccess(null);
  }

  public static <T> ApiResponse<T> ofSuccess(T data){
    return ofStatus(Status.SUCCESS, data);
  }

  public static ApiResponse<?> ofMessage(String message){
    return of(Status.SUCCESS.getCode(), message, null);
  }

  public static <T> ApiResponse<?> ofStatus(Status status){
    return ofStatus(status, null);
  }

  public static <T> ApiResponse<T> ofStatus(Status status, T data){
    return of(status.getCode(), status.getMessage(), data);
  }

  public static <T extends BaseException> ApiResponse<?> ofException(T e){
    return of(e.getCode(), e.getMessage(), e.getData());
  }
}
