package com.zhuzhe.common.entity;

import lombok.Data;

@Data
public class ResultData<T> {
  private int code;
  private String message;
  private T data;

  public ResultData() {
  }

  public static <T> ResultData<T> success(T data){
    ResultData<T> resultData = new ResultData<>();
    resultData.setCode(DataStatus.SUCCESS.getCode());
    resultData.setMessage(DataStatus.SUCCESS.getMessage());
    resultData.setData(data);
    return resultData;
  }

  public static <T> ResultData<T> failure(int code, String message){
    ResultData<T> resultData = new ResultData<>();
    resultData.setCode(code);
    resultData.setMessage(message);
    return resultData;
  }
}
