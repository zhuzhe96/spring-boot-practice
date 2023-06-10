package com.zhuzhe.securityrbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuzhe.securityrbac.entity.Role;
import java.util.Set;

public interface RoleService extends IService<Role> {
  Set<Role> getUserRoles(Long userId);
}
