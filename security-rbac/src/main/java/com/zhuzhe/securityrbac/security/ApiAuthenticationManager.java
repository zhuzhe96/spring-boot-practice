package com.zhuzhe.securityrbac.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.RoleService;
import com.zhuzhe.securityrbac.service.UserService;
import com.zhuzhe.securityrbac.utils.UserLockUtil;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiAuthenticationManager implements AuthenticationManager {

  @Autowired private UserService userService;
  @Autowired private TokenService tokenService;
  @Autowired private PasswordEncoder encoder;
  @Autowired private RoleService roleService;
  @Autowired private UserLockUtil userLockUtil;

  /*自定义登录认证*/
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof ApiAuthenticationToken apiAuthenticationToken)) {
      return null;
    }

    var userPrincipal = new UserPrincipal();
    var username = apiAuthenticationToken.getUserPrincipal().getUsername();
    var password = apiAuthenticationToken.getUserPrincipal().getPassword();
    var user = userService.getOne(new QueryWrapper<User>().eq("username", username));
    if (Objects.isNull(user)) {
      throw new SecurityException(Status.USERNAME_NOT_FOUND);
    }
    var userId = user.getId();

    // 判断当前用户是否已锁定，锁定的情况下不能进行登录
    if (userLockUtil.getLock(userId))
      throw new LockedException(Status.USER_DISABLE.getMessage());

    var presentPassword = user.getPassword();
    log.info("传递的密码={}, 数据库记录的密码={}", password, presentPassword);
    if (!encoder.matches(password, presentPassword)) {
      // 登录失败时获取重试
      var retryCount = userLockUtil.getRetryCount(user.getId());
      if (retryCount!=0)
        throw new SecurityException(Status.USERNAME_PASSWORD_ERROR.custStatusMsg("用户名或密码错误，剩余登录次数: "+retryCount));
      else
        throw new SecurityException(Status.USERNAME_PASSWORD_ERROR.custStatusMsg("用户名或密码错误，重试次数用完,用户已进入锁定."));
    }

    var address = apiAuthenticationToken.getUserPrincipal().getAddress();
    var browser = apiAuthenticationToken.getUserPrincipal().getBrowser();
    userPrincipal.setAddress(address);
    userPrincipal.setBrowser(browser);
    BeanUtils.copyProperties(user, userPrincipal);
    // 登陆成功就设置用户信息到Redis中
    tokenService.refreshRedisToken(userPrincipal);
    user.setLoginTime(new Date());
    userService.update(user, new QueryWrapper<User>().eq("id", user.getId()));
    log.info("[ApiAuthenticationManager]用户登陆成功,user={}", user);
    List<SimpleGrantedAuthority> authorities = null;
    var roles = roleService.getUserRoles(userPrincipal.getId());
    if (CollectionUtils.isNotEmpty(roles))
      authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getKey())).toList();
    return ApiAuthenticationToken.authenticated(userPrincipal, authorities);
  }
}
