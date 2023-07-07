package com.zhuzhe.accessingdatamongodb.controller;

import com.zhuzhe.accessingdatamongodb.entity.User;
import com.zhuzhe.accessingdatamongodb.service.DocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("document")
public class DocumentController {

  @Autowired
  private DocumentService service;

  @PutMapping
  public ResponseEntity<?> put(@RequestBody User user) {
    return ResponseEntity.ok(service.insert(user));
  }

  @PutMapping("batch")
  public ResponseEntity<?> batchPut(@RequestBody List<User> users) {
    return ResponseEntity.ok(service.insert(users));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<?> delete(@PathVariable("userId") String id) {
    return ResponseEntity.ok(service.findAndRemove(id));
  }

  @PatchMapping
  public ResponseEntity<?> patch(@RequestBody User user) {
    return ResponseEntity.ok(service.update(user));
  }

  @GetMapping
  public ResponseEntity<?> get(User user, Pageable pageable) {
    return ResponseEntity.ok(service.find(user, pageable));
  }
}
