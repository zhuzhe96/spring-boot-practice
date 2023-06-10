package com.zhuzhe.securityrbac.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.security.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/*身份校验，校验用户是否登录*/
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private TokenService tokenService;
  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("[JwtAuthenticationFilter]拦截uri={}",request.getRequestURI());
    try {
      var userPrincipal = tokenService.getUserPrincipal(request);
      if (userPrincipal!=null){
        // 校验用户是否有效
        var user = userService.getById(userPrincipal.getId());
        if (user==null){
          throw new SecurityException(Status.USERNAME_NOT_FOUND);
        }
        // 更新用户信息
        BeanUtils.copyProperties(user, userPrincipal);
        log.info("复制后的userPrincipal={}",userPrincipal);

        // 刷新Redis中的用户信息
        ApiAuthenticationToken authenticationToken = null;
        tokenService.verifyTokenExpire(userPrincipal);

        // 判断账号密码
        if (StringUtils.isNotBlank(userPrincipal.getUsername()) && StringUtils.isNotBlank(userPrincipal.getPassword())){
          var authorities = userPrincipal.getRoles().stream()
              .map(SimpleGrantedAuthority::new).toList();
          authenticationToken = ApiAuthenticationToken.authenticated(userPrincipal, authorities);
        }
        // 更新用户Token
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }else {
        log.info("[JwtAuthenticationFilter]Token不存在, 进入下一个过滤器");
      }
      filterChain.doFilter(request, response);
    } catch (RuntimeException e) {
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      response.setStatus(401);
      if (e instanceof SecurityException){
        new ObjectMapper().writeValue(response.getWriter(), Map.of(((SecurityException) e).getCode(), e.getMessage()));
      }else {
        e.printStackTrace();
        new ObjectMapper().writeValue(response.getWriter(), Status.ERROR.custStatusMsg("发生了其他错误"));
      }
    }
  }
}
