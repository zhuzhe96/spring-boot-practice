package com.zhuzhe.accessingdatamongodb.service;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.validation.Validator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CollectionService {

  @Autowired
  private MongoTemplate template;

  // 判断是否存在
  public boolean exists(String collectionName) {
    return template.collectionExists(collectionName);
  }

  // 创建普通集合
  public void create(String collectionName) {
    template.createCollection(collectionName);
    log.info("集合{}创建结果: {}", collectionName, exists(collectionName));
  }

  // 创建指定大小集合
  public void create(String collectionName, long collectionSize, long maxDocumentCount) {
    var options = CollectionOptions.empty().capped().size(collectionSize)
        .maxDocuments(maxDocumentCount);
    template.createCollection(collectionName, options);
    log.info("集合{}创建结果: {}", collectionName, exists(collectionName));
  }

  // 创建带验证集合
  public void create(String collectionName, int age) {
    var criteria = Criteria.where("age").gt(age);
    var options = CollectionOptions.empty().validator(Validator.criteria(criteria))
        .strictValidation().failOnValidationError();
    template.createCollection(collectionName, options);
    log.info("集合{}创建结果: {}", collectionName, exists(collectionName));
  }

  // 获取集合名称
  public Set<String> getName() {
    return template.getCollectionNames();
  }

  // 删除集合
  public void delete(String collectionName) {
    template.getCollection(collectionName).drop();
    log.info("集合{}删除结果: {}", collectionName, !exists(collectionName));
  }
}
