package com.zhuzhe.securityrbac.security.filter;

import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.config.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal.UserPrincipalBuilder;
import com.zhuzhe.securityrbac.exception.BaseException;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired
  private JwtUtil jwtUtil;

  public ApiAuthenticationFilter(AuthenticationManager authenticationManager,
      String loginProcessingUrl) {
    super(authenticationManager);
    setFilterProcessesUrl(loginProcessingUrl);
  }

  /*登陆校验*/
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    //return super.attemptAuthentication(request, response);
    log.info("[ApiAuthenticationFilter]请求uri={}",request.getRequestURI());
    // 登陆提交只能post
    if (!"POST".equals(request.getMethod())){
      throw new SecurityException(Status.REQUEST_NOT_MATCH);
    }

    var username = request.getParameter("username");
    var password = request.getParameter("password");

    var userPrincipal = UserPrincipal.builder();
    ApiAuthenticationToken apiAuthenticationToken;

    // 登录校验
    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
      userPrincipal.username(username).password(password);
      apiAuthenticationToken = ApiAuthenticationToken.unauthenticated(userPrincipal.build());
      var authenticate = getAuthenticationManager().authenticate(apiAuthenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
      return authenticate;
    }else {
      throw new SecurityException(Status.USERNAME_PASSWORD_ERROR);
    }
  }

  /*成功时的处理*/
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    ApiAuthenticationToken token = (ApiAuthenticationToken)authResult;
    super.successfulAuthentication(request, response, chain, authResult);
  }

  /*失败时的处理*/
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
