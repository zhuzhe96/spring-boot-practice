package com.zhuzhe.securityrbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhe.securityrbac.entity.Permission;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface PermissionMapper extends BaseMapper<Permission> {
  public List<Permission> getUserPermissions(Long userId);
}
