package com.zhuzhe.securityrbac.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiAuthenticationManager implements AuthenticationManager {



  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return null;
  }
}
