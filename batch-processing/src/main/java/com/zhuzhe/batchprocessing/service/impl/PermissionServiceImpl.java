package com.zhuzhe.batchprocessing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.batchprocessing.entity.Permission;
import com.zhuzhe.batchprocessing.mapper.PermissionMapper;
import com.zhuzhe.batchprocessing.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements
    PermissionService {

}
