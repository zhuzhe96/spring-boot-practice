package com.zhuzhe.securityrbac.entity.vo;

public record JwtInfo(String token, String tokenType) {
  public JwtInfo(String token) {
    this(token, "Bearer");
  }
}