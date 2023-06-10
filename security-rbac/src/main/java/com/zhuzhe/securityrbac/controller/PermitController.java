package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.common.Status;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("permit")
public class PermitController {

  @GetMapping
  public ResponseEntity<?> permitRequest() {
    return ResponseEntity.ok(Map.of("code", Status.SUCCESS.getCode(), "message",
        Status.SUCCESS.custStatusMsg("这个请求不用验证").getMessage()));
  }
}
