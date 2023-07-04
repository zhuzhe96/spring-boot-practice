package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.entity.Network;
import com.zhuzhe.integrationmqtt.entity.Watch;
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
  private WatchService service;

  @GetMapping("{deviceId:\\d+}")
  public ResponseEntity<Watch> get(@PathVariable("deviceId") Long id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping("network/{sn}/{mac}/{wifiName}")
  public DeferredResult<?> getNetwork(@PathVariable String sn, @PathVariable String mac, @PathVariable String wifiName){
    return service.getNetwork(sn, mac, wifiName);
  }

  @PostMapping("network/{sn}/{mac}")
  public DeferredResult<?> setNetwork(@PathVariable String sn,@PathVariable String mac, @RequestBody Network network){
    return service.setNetwork(sn,mac,network);
  }
}
