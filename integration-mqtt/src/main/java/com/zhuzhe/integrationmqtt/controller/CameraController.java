package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.service.camera.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("camera")
public class CameraController {
  @Autowired private CameraService service;

  @GetMapping("{sn}/{mac}")
  public DeferredResult<?> get(@PathVariable("sn") String sn, @PathVariable("mac") String mac) {
    return service.get(sn, mac);
  }
}
