package com.zhuzhe.securityrbac.handler;

import com.zhuzhe.securityrbac.config.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class ApiAuthSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private TokenService tokenService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    var authenticationToken = (ApiAuthenticationToken) authentication;
    var jwt = tokenService.createJwt(authenticationToken.getUserPrincipal());
    return;
  }
}
