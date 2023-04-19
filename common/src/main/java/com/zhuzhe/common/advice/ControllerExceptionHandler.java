package com.zhuzhe.common.advice;

import com.zhuzhe.common.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/*全局Controller异常处理*/
@RestControllerAdvice
public class ControllerExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler(StorageException.class)
  public ResponseEntity<?> handleStorage(StorageException e) {
    log.info("全局异常处理: 文件上传异常"+e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
    log.info("全局异常处理: 上传文件大小限制"+e);
    // 这里能成功处理返回的关键是server.tomcat.max-swallow-size=-1 zhuzhe zhuzhe zhuzhe
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body("上传文件超过服务器限制,上传失败");
  }
}
