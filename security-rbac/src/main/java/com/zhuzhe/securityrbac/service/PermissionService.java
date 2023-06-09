package com.zhuzhe.securityrbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuzhe.securityrbac.entity.Permission;
import java.util.List;

public interface PermissionService extends IService<Permission> {
  public List<Permission> getUserPermissions(Long userId);
}
