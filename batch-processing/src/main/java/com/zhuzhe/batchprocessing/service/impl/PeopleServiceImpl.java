package com.zhuzhe.batchprocessing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuzhe.batchprocessing.mapper.PeopleMapper;
import com.zhuzhe.batchprocessing.service.PeopleService;
import com.zhuzhe.batchprocessing.entity.People;
import org.springframework.stereotype.Service;

@Service
public class PeopleServiceImpl extends ServiceImpl<PeopleMapper, People> implements PeopleService {

}
