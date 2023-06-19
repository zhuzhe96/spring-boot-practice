package com.zhuzhe.securityrbac.security.handler;

import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiLogoutSuccessHandler implements LogoutSuccessHandler {
  @Autowired private ResponseUtil responseUtil;
  @Autowired private TokenService tokenService;

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    var userPrincipal = tokenService.getUserPrincipal(request);
    if (userPrincipal == null) {
      responseUtil.renderJson(
          response,
          HttpStatus.BAD_REQUEST,
          ApiResponse.of(Status.NOT_LOGIN.getCode(), "当前用户已退出登陆,请勿重复操作!", null));
    } else {
      tokenService.invalidateToken(userPrincipal.getId());
      responseUtil.renderJson(response, HttpStatus.OK, ApiResponse.ofStatus(Status.SUCCESS));
    }
  }
}
