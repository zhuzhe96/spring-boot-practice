package com.zhuzhe.securityrbac.security.filter;

import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.config.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.UserService;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain){
    log.info("[JwtAuthenticationFilter]处理JWT验证,请求uri={}",request.getRequestURI());
    try {
      var userPrincipal = tokenService.getUserPrincipal(request);
      if (userPrincipal!=null){
        var user = userService.getById(userPrincipal.getId());
        if (user==null){
          throw new SecurityException(Status.USERNAME_NOT_FOUND);
        }
        // 更新用户信息
        BeanUtils.copyProperties(user, userPrincipal);
        log.info("userPrincipal={}",userPrincipal);

        // 更新token
        ApiAuthenticationToken authenticationToken = null;
        tokenService.verifyTokenExpire(userPrincipal);
        userService.save(user);

        // 判断账号密码
        if (StringUtils.isNotBlank(userPrincipal.getUsername()) && StringUtils.isNotBlank(userPrincipal.getPassword())){
          authenticationToken = ApiAuthenticationToken.authenticated(userPrincipal, null);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }else {
        log.info("[JwtAuthenticationFilter]Token不存在, 进入下一个过滤器");
      }
      filterChain.doFilter(request, response);
    } catch (SecurityException e) {
      throw e;
    } catch (BeansException | IOException | ServletException e) {
      throw new RuntimeException("发生其他错误",e);
    }
  }
}
