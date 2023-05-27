package com.zhuzhe.common.advice;

import com.zhuzhe.common.entity.DataStatus;
import com.zhuzhe.common.entity.ResultData;
import com.zhuzhe.common.exception.StorageException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/*全局Controller异常处理*/
@RestControllerAdvice
public class ControllerExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler(StorageException.class)
  public ResponseEntity<?> handleStorage(StorageException e) {
    log.info("全局异常处理: 文件上传异常" + e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
    log.info("全局异常处理: 上传文件大小限制" + e);
    // 这里能成功处理返回的关键是server.tomcat.max-swallow-size=-1 zhuzhe zhuzhe zhuzhe
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body("上传文件超过服务器限制,上传失败");
  }

  /*数据校验,自定义校验器*/
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
    StringBuilder sb = new StringBuilder();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      sb.append(violation.getMessage()).append("\n");
    }
    return ResponseEntity.badRequest().body(sb.toString());
  }

  /*数据校验,校验失败时的提示*/
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    var sb = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      if (!sb.isEmpty()) {
        sb.append(", ");
      }
      String errorMessage = error.getDefaultMessage();
      if (error instanceof FieldError) {
        String fieldName = ((FieldError) error).getField();
        sb.append(fieldName).append(errorMessage);
      } else {
        sb.append(error.getObjectName()).append(errorMessage);
      }
    });
    return ResponseEntity.badRequest()
        .body(ResultData.failure(DataStatus.INVALID_INPUT.getCode(), sb.toString()));
  }
}
