package com.zhuzhe.accessingdatamongodb.service;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

// 视图（Views）是基于一个或多个集合的查询结果而创建的虚拟集合
@Slf4j
@Service
public class ViewService {

  @Autowired private MongoTemplate template;

  public boolean exists(String collectionName) {
    return template.collectionExists(collectionName);
  }

  public void create(String viewName, String collectionName, String conditions) {
    var pipeline = new ArrayList<Bson>();
    pipeline.add(Document.parse(conditions));
    template.getDb().createView(viewName, collectionName, pipeline);
    log.info("视图{}创建结果: {}", viewName, exists(collectionName));
  }

  public void delete(String viewName){
    if (exists(viewName)){
      template.getDb().getCollection(viewName).drop();
    }
    log.info("视图{}删除结果: {}", viewName, exists(viewName));
  }
}
