package com.zhuzhe.accessingdatamongodb.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings("unused")
public class IndexService {

  public static final String COLLECTION_NAME = "users";
  @Resource
  private MongoTemplate template;

  // 增量索引
  public String incrementIndex(String fieldName) {
    return template.getCollection(COLLECTION_NAME).createIndex(Indexes.ascending(fieldName));
  }

  // 减量索引
  public String decrementIndex(String fieldName) {
    return template.getCollection(COLLECTION_NAME).createIndex(Indexes.descending(fieldName));
  }

  // 复合索引
  public String complexIndex(String fieldName1, String fieldName2) {
    return template.getCollection(COLLECTION_NAME)
        .createIndex(Indexes.ascending(fieldName1, fieldName2));
  }

  // 文字索引
  public String textIndex(String fieldName) {
    return template.getCollection(COLLECTION_NAME).createIndex(Indexes.text(fieldName));
  }

  // 哈希索引
  public String hashIndex(String fieldName) {
    return template.getCollection(COLLECTION_NAME).createIndex(Indexes.hashed(fieldName));
  }

  // 唯一索引（升序）
  public String uniqueIndex(String fieldName) {
    var options = new IndexOptions();
    options.unique(true);
    return template.getCollection(COLLECTION_NAME)
        .createIndex(Indexes.ascending(fieldName), options);
  }

  // 局部索引（升序）
  public String partialIndex(String fieldName) {
    var options = new IndexOptions();
    options.partialFilterExpression(Filters.exists(fieldName, true));
    return template.getCollection(COLLECTION_NAME)
        .createIndex(Indexes.ascending(fieldName), options);
  }

  // 查询当前集合的所有索引信息
  public List<Document> findAll() {
    var indexes = template.getCollection(COLLECTION_NAME).listIndexes();
    var documents = new ArrayList<Document>();
    indexes.forEach(documents::add);
    return documents;
  }

  // 删除指定索引
  public void remove(String indexName) {
    template.getCollection(COLLECTION_NAME).dropIndex(indexName);
  }

  // 删除全部索引
  public void removeAll() {
    template.getCollection(COLLECTION_NAME).dropIndexes();
  }
}
