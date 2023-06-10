package com.zhuzhe.securityrbac.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.securityrbac.entity.Role;
import com.zhuzhe.securityrbac.entity.UserRole;
import com.zhuzhe.securityrbac.mapper.UserRoleMapper;
import com.zhuzhe.securityrbac.security.service.TokenService;
import com.zhuzhe.securityrbac.service.RoleService;
import com.zhuzhe.securityrbac.service.UserRoleService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements
    UserRoleService {

  @Autowired
  private RoleService roleService;
  @Autowired
  private TokenService tokenService;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void bindUserRole(UserRole userRole) {
    baseMapper.insert(userRole);
    updateRedisUser(userRole.userId());
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void unbindUserRole(UserRole userRole) {
    baseMapper.deleteById(userRole);
    updateRedisUser(userRole.userId());
  }

  private void updateRedisUser(Long userId){
    var userPrincipal = tokenService.getUserPrincipal(userId);
    var roles = roleService.getUserRoles(userId);

    Set<String> roleSet = new HashSet<>();
    if (CollectionUtils.isNotEmpty(roles)){
      roleSet = roles.stream().map(Role::getKey).collect(Collectors.toSet());
    }
    userPrincipal.setRoles(roleSet);
    tokenService.setUserPrincipal(userPrincipal);
  }
}
