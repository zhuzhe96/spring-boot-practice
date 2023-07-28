package com.zhuzhe.accessingdatamongodb.controller;

import com.zhuzhe.accessingdatamongodb.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("command")
public class CommandController {

  @Autowired
  private CommandService commandService;

  @GetMapping
  public ResponseEntity<?> runCommand() {
    return ResponseEntity.ok(
        commandService.runCommand("{find:'users',filter:{age:{$gt:13}}}"));
  }
}
