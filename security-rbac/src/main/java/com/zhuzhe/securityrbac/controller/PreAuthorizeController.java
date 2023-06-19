package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pre-authorize")
public class PreAuthorizeController {

  @GetMapping("product")
  @PreAuthorize("hasRole('operator') and hasPermission('', 'product') and hasPermission('','content')")
  public ResponseEntity<?> productOnly(){
    return ResponseEntity.ok(ApiResponse.ofSuccess("拥有operator角色和product权限, 可以访问"));
  }
}
