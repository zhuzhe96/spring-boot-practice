package com.zhuzhe.accessingdatamongodb.controller;

import com.zhuzhe.accessingdatamongodb.service.CollectionService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("collection")
public class CollectionController {
  @Autowired private CollectionService service;

  record CollectionParam(String name, Long size, Long count){}
  @PutMapping
  public ResponseEntity<?> put(@RequestBody CollectionParam param){
    // 创建一个1KB的集合，可以容纳10个文档
    service.create(param.name, param.size, param.count);
    var result = service.exists(param.name);
    if (result){
      return ResponseEntity.ok(service.getName());
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping("{collectionName}")
  public ResponseEntity<?> delete(@PathVariable String collectionName){
    service.delete(collectionName);
    var result = service.exists(collectionName);
    if (!result){
      return ResponseEntity.ok(Map.of("code",200,"message","delete success"));
    }
    return ResponseEntity.badRequest().build();
  }
}
