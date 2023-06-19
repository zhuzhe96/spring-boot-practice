package com.zhuzhe.securityrbac.security.filter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.Role;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.RoleService;
import com.zhuzhe.securityrbac.service.UserService;
import com.zhuzhe.securityrbac.utils.ResponseUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/*身份校验，校验用户是否登录*/
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired private TokenService tokenService;
  @Autowired private UserService userService;
  @Autowired private RoleService roleService;
  @Autowired private ResponseUtil responseUtil;
  @Autowired private List<String> ignoredUrls;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("进入jwt过滤器, uri: {}", request.getRequestURI());
    if (ignoredUrls.contains(request.getRequestURI())){
      filterChain.doFilter(request, response);
      return;
    }


    var address = request.getRemoteAddr();
    var browser = request.getHeader("User-Agent");

    try {
      // 每次访问时先从Redis获得用户，再去构建这个用户线程的SecurityContext
      var userPrincipal = tokenService.getUserPrincipal(request);

      if (userPrincipal != null) {
        /*校验用户是否异地登陆*/
        if (!userPrincipal.getAddress().equals(address)
            || !userPrincipal.getBrowser().equals(browser))
          throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);

        // 校验用户是否有效
        var user = userService.getById(userPrincipal.getId());
        if (user == null) {
          throw new SecurityException(Status.USERNAME_NOT_FOUND);
        }

        // 如果是已经登陆还重复登陆的,进行拦截
        if ("/user/login".equals(request.getRequestURI()))
          throw new SecurityException(Status.ERROR, "用户已登陆, 请勿重复操作");
        // 同步数据库中的用户
        BeanUtils.copyProperties(user, userPrincipal);
        log.info("复制后的userPrincipal={}", userPrincipal);

        // 刷新Redis中的用户信息
        ApiAuthenticationToken authenticationToken = null;
        tokenService.verifyTokenExpire(userPrincipal);

        // 更新用户的SecurityContext中的对象
        if (StringUtils.isNotBlank(userPrincipal.getUsername())
            && StringUtils.isNotBlank(userPrincipal.getPassword())) {
          List<SimpleGrantedAuthority> authorities = null;
          var roles = roleService.getUserRoles(userPrincipal.getId());
          if (CollectionUtils.isNotEmpty(roles))
            authorities =
                roles.stream().map(Role::getKey).map(SimpleGrantedAuthority::new).toList();
          authenticationToken = ApiAuthenticationToken.authenticated(userPrincipal, authorities);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
      log.info("jwt过滤器通过, 进入下一个过滤器");
      filterChain.doFilter(request, response);
    } catch (RuntimeException e) {
      if (e instanceof SecurityException securityException) {
        if (securityException.getCode() == (int) Status.REQUEST_NOT_MATCH.getCode()) {
          responseUtil.renderJson(response, HttpStatus.METHOD_NOT_ALLOWED, (SecurityException) e);
        } else {
          responseUtil.renderJson(response, HttpStatus.BAD_REQUEST, (SecurityException) e);
        }
      } else {
        responseUtil.renderJson(
            response, HttpStatus.BAD_REQUEST, ApiResponse.ofStatus(Status.PARAM_NOT_MATCH));
      }
    }
  }
}
