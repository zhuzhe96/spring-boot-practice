package com.zhuzhe.securityrbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.securityrbac.entity.UserRole;
import com.zhuzhe.securityrbac.mapper.UserRoleMapper;
import com.zhuzhe.securityrbac.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {}
