package com.zhuzhe.quartztasks.controller;

import com.zhuzhe.quartztasks.service.CallbackImpl;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RequestMapping("test")
@RestController
public class TestController {

  @GetMapping("01")
  public DeferredResult<?> test01(){
    var deferredResult =
        new DeferredResult<>(
            3000L,
            () -> new RuntimeException("与设备的连接已断开, 请重启设备之后重试!"));
    new Thread(()->{
      try {
        Thread.sleep(2000);
        deferredResult.setResult("success");
      } catch (InterruptedException e) {
        deferredResult.setErrorResult("failed");
      }
    }).start();
    return deferredResult;
  }

  @GetMapping("02")
  public Future<?> test02(){
    var future = new CompletableFuture<>();
    // future.completeOnTimeout("timeout", 3000, TimeUnit.SECONDS);
    System.out.println("start");
    try {
      Thread.sleep(4000);
      future.complete("success-zz");
    } catch (InterruptedException e) {
      future.complete("failed");
      throw new RuntimeException(e);
    }
    System.out.println("end");
    return future;
  }

  private void sendMsg(CallbackImpl impl){
    impl.callback(new byte[100]);
  }
}
