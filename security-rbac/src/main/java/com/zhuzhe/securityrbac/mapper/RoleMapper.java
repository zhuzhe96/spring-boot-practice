package com.zhuzhe.securityrbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhe.securityrbac.entity.Role;
import java.util.Set;

public interface RoleMapper extends BaseMapper<Role> {
  Set<Role> getUserRoles(Long userId);
}
