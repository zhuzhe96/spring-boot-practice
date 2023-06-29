package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.entity.Device;
import com.zhuzhe.integrationmqtt.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("device")
public class DeviceController {
  @Autowired
  private DeviceService service;
  
  @GetMapping("{id:\\d+}")
  public ResponseEntity<?> get(@PathVariable Long id) {
    return ResponseEntity.ok(service.get(id));
  }

  @PostMapping
  public ResponseEntity<?> post(@RequestBody Device device){
    service.add(device);
    return ResponseEntity.ok(null);
  }
}
