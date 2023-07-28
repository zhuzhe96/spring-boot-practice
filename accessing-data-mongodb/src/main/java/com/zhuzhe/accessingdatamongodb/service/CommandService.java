package com.zhuzhe.accessingdatamongodb.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandService {

  @Resource
  private MongoTemplate template;


  /**
   * 运行指定命令
   * <a href="https://www.mongodb.com/docs/manual/reference/command/">MongoDB命令</a>
   */
  public Document runCommand(String jsonCommand) {
    var command = Document.parse(jsonCommand);
    return template.getDb().runCommand(command);
  }
}
