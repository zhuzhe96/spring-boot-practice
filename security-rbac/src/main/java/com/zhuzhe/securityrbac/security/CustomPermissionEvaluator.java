package com.zhuzhe.securityrbac.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator  {

  /*定义用户操作方法时,需要拥有对应权限*/
  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    var userPrincipal = ((ApiAuthenticationToken) authentication).getUserPrincipal();
    return userPrincipal.getPermissions().stream().anyMatch(p-> StringUtils.equals(p,String.valueOf(permission)));
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return hasPermission(authentication,null, permission);
  }
}
