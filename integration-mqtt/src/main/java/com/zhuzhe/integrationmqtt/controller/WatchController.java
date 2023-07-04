package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.service.pubsub.WatchPublishService;
import com.zhuzhe.integrationmqtt.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("watch")
public class WatchController {
  
  @Autowired
  private WatchPublishService watchPublishService;
  @Autowired
  private WatchService watchService;

  @GetMapping("network/{sn}/{mac}/{wifiName}")
  public DeferredResult<Network> get(@PathVariable String sn, @PathVariable String mac, @PathVariable String wifiName){
    return watchService.getNetwork(sn, mac, wifiName);
  }

  // 这里的发布订阅只是一种简单尝试，实际上使用上面的异步回调可以做到所有的get和set操作
  @PostMapping("network/{sn}/{mac}")
  public ResponseEntity<?> post(@PathVariable String sn,@PathVariable String mac, @RequestBody Network network){
    watchPublishService.setNetWork(sn,mac,network);
    return ResponseEntity.ok(null);
  }
}
