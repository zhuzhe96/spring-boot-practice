package com.zhuzhe.securityrbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.security.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserService service;
  @Autowired private PasswordEncoder encoder;

  /*获取当前登陆用户信息*/
  @GetMapping
  public ResponseEntity<?> get(){
    return ResponseEntity.ok(ApiResponse.ofSuccess((ApiAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()));
  }

  /*查询用户列表*/
  @GetMapping("list")
  public ResponseEntity<?> list(){
    return ResponseEntity.ok(ApiResponse.ofSuccess(service.list()));
  }

  /*新增用户信息*/
  @PostMapping("register")
  public ResponseEntity<?> post(@RequestBody User user){
    var count = service.count(new QueryWrapper<User>().eq("username", user.getUsername()));
    if (count > 0)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.ofStatus(Status.USERNAME_EXIST));
    user.setPassword(encoder.encode(user.getPassword()));
    service.save(user);
    return ResponseEntity.ok(ApiResponse.ofSuccess());
  }
}
