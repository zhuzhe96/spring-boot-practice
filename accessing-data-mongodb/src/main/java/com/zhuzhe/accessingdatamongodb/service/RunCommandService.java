package com.zhuzhe.accessingdatamongodb.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings("unused")
public class RunCommandService {
  @Resource
  private MongoTemplate template;

  /**
   * 运行指定命令
   */
  public Document runCommand(String jsonCommand){
    var command = Document.parse(jsonCommand);
    return template.getDb().runCommand(command);
  }
}
