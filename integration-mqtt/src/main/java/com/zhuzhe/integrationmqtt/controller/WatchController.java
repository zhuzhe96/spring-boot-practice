package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.service.WatchPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("watch")
public class WatchController {
  
  @Autowired
  private WatchPublishService service;
  
  @PostMapping("network/{sn}/{mac}")
  public ResponseEntity<?> post(@PathVariable String sn,@PathVariable String mac, @RequestBody Network network){
    service.setNetWork(sn,mac,network);
    return ResponseEntity.ok(null);
  }
}
