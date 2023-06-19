package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("permit")
public class PermitController {

  @GetMapping
  public ResponseEntity<?> permitRequest() {
    return ResponseEntity.ok(ApiResponse.ofSuccess("这个请求不用校验登陆!"));
  }
}
