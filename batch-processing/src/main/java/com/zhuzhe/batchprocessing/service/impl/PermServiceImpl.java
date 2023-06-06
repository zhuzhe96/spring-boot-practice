package com.zhuzhe.batchprocessing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.batchprocessing.entity.Perm;
import com.zhuzhe.batchprocessing.mapper.PermMapper;
import com.zhuzhe.batchprocessing.service.PermService;
import org.springframework.stereotype.Service;

@Service
public class PermServiceImpl extends ServiceImpl<PermMapper, Perm> implements PermService {

}
