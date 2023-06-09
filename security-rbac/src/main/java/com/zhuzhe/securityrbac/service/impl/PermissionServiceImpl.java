package com.zhuzhe.securityrbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.securityrbac.entity.Permission;
import com.zhuzhe.securityrbac.mapper.PermissionMapper;
import com.zhuzhe.securityrbac.service.PermissionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements
    PermissionService {

  @Override
  public List<Permission> getUserPermissions(Long userId) {
    return baseMapper.getUserPermissions(userId);
  }
}
