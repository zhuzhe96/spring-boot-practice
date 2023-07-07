package com.zhuzhe.accessingdatamongodb;

import com.zhuzhe.accessingdatamongodb.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
@SpringBootTest
class AccessingDataMongodbApplicationTests {

  public static final String COLLECTION_NAME = "users";
  @Autowired
  private MongoTemplate template;

  @Test
  void contextLoads() {
    var users = template.find(new Query(new Criteria().and("name").is("zhuzhe").and("age").is("13")), User.class,
        COLLECTION_NAME);
    log.info("users={}", users);
  }

}
