package com.zhuzhe.accessingdatamongodb.service;

import com.zhuzhe.accessingdatamongodb.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@SuppressWarnings("all")
public class TransactionService {
  public static final String COLLECTION_NAME = "users";
  @Resource
  private MongoTemplate template;

  @Transactional(rollbackFor = Exception.class)
  public User testInsert(User user){
    var result = template.insert(user, COLLECTION_NAME);
    // 模拟异常
    int error = 1/0;
    return result;
  }
}
