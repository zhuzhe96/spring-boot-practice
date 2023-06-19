package com.zhuzhe.securityrbac.security.filter;

import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.vo.JwtInfo;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.utils.ResponseUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 登陆过滤器
@Slf4j
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired private TokenService tokenService;
  @Autowired private ResponseUtil responseUtil;

  public ApiAuthenticationFilter(
      AuthenticationManager authenticationManager, String loginProcessingUrl) {
    super(authenticationManager);
    setFilterProcessesUrl(loginProcessingUrl);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.info("登陆过滤器, uri: {}", request.getRequestURI());

    if (!"POST".equals(request.getMethod())) {
      throw new SecurityException(Status.REQUEST_NOT_MATCH);
    }

    var username = request.getParameter("username");
    var password = request.getParameter("password");
    var address = request.getRemoteAddr();
    var browser = request.getHeader("User-Agent");

    // 登录校验
    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
      var userPrincipal =
          UserPrincipal.builder()
              .username(username)
              .password(password)
              .address(address)
              .browser(browser)
              .build();
      ApiAuthenticationToken apiAuthenticationToken =
          ApiAuthenticationToken.unauthenticated(userPrincipal);
      var authenticate = getAuthenticationManager().authenticate(apiAuthenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
      return authenticate;
    } else {
      throw new SecurityException(Status.USERNAME_PASSWORD_ERROR);
    }
  }

  /*校验成功时的处理: 发token给用户，将用户信息存储到Redis*/
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication) {
    log.info("登陆成功, 开始发令牌.");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ApiAuthenticationToken apiAuthenticationToken = (ApiAuthenticationToken) authentication;
    var jwt = tokenService.createJwt(apiAuthenticationToken.getUserPrincipal());
    responseUtil.renderJson(response, HttpStatus.OK, ApiResponse.ofSuccess(new JwtInfo(jwt)));
  }

  /*校验失败时的处理*/
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    // 这里不能继续往上抛，再抛就会转为另一种通用异常，这将导致无法异常判断
    log.info("登陆失败, 原因: {}", e.getMessage());
    SecurityContextHolder.clearContext();
    if (e instanceof UsernameNotFoundException) {
      responseUtil.renderJson(
          response, HttpStatus.BAD_REQUEST, ApiResponse.ofStatus(Status.USERNAME_PASSWORD_ERROR));
    } else if (e instanceof LockedException) {
      responseUtil.renderJson(
          response, HttpStatus.FORBIDDEN, ApiResponse.ofStatus(Status.USER_DISABLE));
    } else if (e instanceof BadCredentialsException) {
      responseUtil.renderJson(
          response, HttpStatus.BAD_REQUEST, ApiResponse.ofStatus(Status.USERNAME_PASSWORD_ERROR));
    } else {
      responseUtil.renderJson(
          response,
          HttpStatus.INTERNAL_SERVER_ERROR,
          ApiResponse.of(Status.ERROR.getCode(), e.getMessage(), null));
    }
  }
}
