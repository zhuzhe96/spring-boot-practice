package com.zhuzhe.integrationmqtt.controller;

import com.zhuzhe.integrationmqtt.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqtt/client")
public class ClientController {

  @Autowired
  private ClientService clientService;

  @GetMapping("send")
  public ResponseEntity<?> sendMqttMessage(){
    return ResponseEntity.ok(clientService.sendMqttMessage());
  }
}
