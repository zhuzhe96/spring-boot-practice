package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.common.Status;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pre-authorize")
public class PreAuthorizeController {

  @GetMapping("product")
  @PreAuthorize("hasRole('admin') and hasPermission('', 'product')")
  public ResponseEntity<?> productOnly(){
    return ResponseEntity.ok(Map.of("code", Status.SUCCESS.getCode(), "message",
        Status.SUCCESS.custStatusMsg("产品管理接口").getMessage()));
  }
}
