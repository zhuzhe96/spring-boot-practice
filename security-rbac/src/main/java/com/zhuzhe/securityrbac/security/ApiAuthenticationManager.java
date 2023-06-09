package com.zhuzhe.securityrbac.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.Permission;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal.UserPrincipalBuilder;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.PermissionService;
import com.zhuzhe.securityrbac.service.UserService;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiAuthenticationManager implements AuthenticationManager {
  @Autowired
  private UserService userService;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private PermissionService permissionService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    ApiAuthenticationToken apiAuthenticationToken = null;

    if (!(authentication instanceof ApiAuthenticationToken)){
      return null;
    }

    apiAuthenticationToken = (ApiAuthenticationToken) authentication;
    var userPrincipal = new UserPrincipal();

    var username = apiAuthenticationToken.getUserPrincipal().getUsername();
    var password = apiAuthenticationToken.getUserPrincipal().getPassword();
    var user = userService.getOne(new QueryWrapper<User>().eq("username", username));
    if (Objects.isNull(user)) throw new SecurityException(Status.USERNAME_NOT_FOUND);

    var presentPassword = user.getPassword();
    log.info("传递的密码={}, 数据库记录的密码={}",password, presentPassword);
    if (!encoder.matches(password,presentPassword)) throw new SecurityException(Status.USERNAME_PASSWORD_ERROR);

    BeanUtils.copyProperties(user, userPrincipal);
    var permissions = getUserRolePermissions(user);
    userPrincipal.setPermissions(permissions);
    // 验证令牌是否过期，快过期就续期
    tokenService.verifyTokenExpire(userPrincipal);
    user.setLoginTime(new Date());
    userService.update(user, new QueryWrapper<User>().eq("id",user.getId()));
    log.info("[ApiAuthenticationManager]用户登陆成功,user={}",user);
    return ApiAuthenticationToken.authenticated(userPrincipal,null);
  }

  public Set<String> getUserRolePermissions(User user){
    return permissionService.getUserPermissions(user.getId()).stream().map(Permission::getUrl).collect(
        Collectors.toSet());
  }
}
