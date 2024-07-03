package com.zhuzhe.accessingdatajpa.controller;

import com.zhuzhe.accessingdatajpa.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("device")
public class DeviceController {
  private final DeviceService deviceService;

  @GetMapping("list")
  public ResponseEntity<?> getList(String key, String sort, Integer page, Integer size) {
    return ResponseEntity.ok(deviceService.getList(key, sort, page, size));
  }
}
