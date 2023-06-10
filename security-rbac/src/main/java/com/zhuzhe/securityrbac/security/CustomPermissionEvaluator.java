package com.zhuzhe.securityrbac.security;

import com.zhuzhe.securityrbac.service.PermissionService;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator  {

  @Autowired
  private PermissionService permissionService;

  /*定义用户操作方法时,需要拥有对应权限*/
  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    var userPrincipal = ((ApiAuthenticationToken) authentication).getUserPrincipal();
    var permissions = permissionService.getUserPermissions(userPrincipal.getId());
    if (CollectionUtils.isEmpty(permissions)) return false;
    return permissions.stream().anyMatch(p -> p.getUrl().startsWith(String.valueOf(permission)));
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return hasPermission(authentication,null, permission);
  }
}
