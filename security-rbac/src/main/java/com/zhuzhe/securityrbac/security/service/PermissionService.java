package com.zhuzhe.securityrbac.security.service;

import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.utils.ServletUtil;
import io.micrometer.common.util.StringUtils;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PermissionService {
  public static final String ALL_PERMISSIUON = "*:*:*";
  @Autowired
  private TokenService tokenService;

  /*判断当前用户是否拥有该权限*/
  public boolean hasPermission(String permission){
    if (StringUtils.isBlank(permission)) return false;
    var userPrincipal = tokenService.getUserPrincipal(ServletUtil.getRequest());
    if (userPrincipal==null || CollectionUtils.isEmpty(userPrincipal.getPermissions())){
      return false;
    }
    return hasPermission(userPrincipal.getPermissions(), permission);
  }

  /*判断权限是否在列表中*/
  public boolean hasPermission(Set<String> permissions, String permission){
    return permissions.contains(ALL_PERMISSIUON) || permissions.contains(permission.trim());
  }

  /*判断用户是否没有该权限*/
  public boolean lackPermission(String permission){
    return !hasPermission(permission);
  }

  /*判断用户是否用户该权限，多个*/
  public boolean hasAnyPermission(String permissions){
    if (StringUtils.isEmpty(permissions)){
      return false;
    }
    var userPrincipal = tokenService.getUserPrincipal(ServletUtil.getRequest());
    if (userPrincipal==null || CollectionUtils.isEmpty(userPrincipal.getPermissions())){
      return false;
    }
    for (String permission : permissions.split(",")){
      if (permission!=null && hasPermission(userPrincipal.getPermissions(), permission)){
        return true;
      }
    }
    return false;
  }
}
