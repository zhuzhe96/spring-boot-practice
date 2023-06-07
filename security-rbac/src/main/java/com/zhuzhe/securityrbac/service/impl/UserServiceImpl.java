package com.zhuzhe.securityrbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.securityrbac.entity.User;
import com.zhuzhe.securityrbac.mapper.UserMapper;
import com.zhuzhe.securityrbac.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
