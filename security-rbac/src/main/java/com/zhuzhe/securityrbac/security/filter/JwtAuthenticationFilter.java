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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

    var uri = request.getRequestURI();
    log.info("进入jwt过滤器, uri: {}", request.getRequestURI());
    // 放行指定请求
    try {
      if (!ignoredUrls.contains(uri)) {
        var userPrincipal = tokenService.getUserPrincipal(request);
        if (userPrincipal != null) {
          // 校验登陆用户是否有效
          var user = userService.getById(userPrincipal.getId());
          if (user == null) {
            throw new SecurityException(Status.USERNAME_NOT_FOUND);
          }
          // 每次访问都校验浏览器和ip,如果在别处登陆,则当前的用户请求都将拦截
          var address = request.getRemoteAddr();
          var browser = request.getHeader("User-Agent");
          if (!userPrincipal.getAddress().equals(address)
              || !userPrincipal.getBrowser().equals(browser))
            throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);
          // 判断是否重复登陆
          if ("/user/login".equals(request.getRequestURI()))
            throw new SecurityException(Status.ERROR, "用户已登陆, 请勿重复操作");
          // 同步数据
          BeanUtils.copyProperties(user, userPrincipal);
          ApiAuthenticationToken authenticationToken = null;
          tokenService.verifyTokenExpire(userPrincipal);
          // 进行登陆校验
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
      }
      log.info("jwt过滤器通过, 进入下一个过滤器");
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.info("jwt过滤器发生异常");
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
