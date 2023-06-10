package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService service;

  @GetMapping
  public ResponseEntity<?> get(){
    return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
  }

  @GetMapping("list")
  public ResponseEntity<?> list(){
    return ResponseEntity.ok(service.list());
  }
}
