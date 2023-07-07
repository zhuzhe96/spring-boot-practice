package com.zhuzhe.accessingdatamongodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class TransactionConfig {
  @Bean
  MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory factory){
    return new MongoTransactionManager(factory);
  }
}
