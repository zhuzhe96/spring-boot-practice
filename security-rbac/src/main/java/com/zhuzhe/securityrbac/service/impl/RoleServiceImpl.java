package com.zhuzhe.securityrbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.securityrbac.entity.Role;
import com.zhuzhe.securityrbac.mapper.RoleMapper;
import com.zhuzhe.securityrbac.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
