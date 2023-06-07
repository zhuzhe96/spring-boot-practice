package com.zhuzhe.securityrbac.controller;

import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.UserService;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private UserService userService;
  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ApiResponse<?> login(@RequestBody User user){
    log.info("login...1");
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    log.info("login...2");
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    log.info("login...3");
    return ApiResponse.ofSuccess(Status.SUCCESS);
  }
}
