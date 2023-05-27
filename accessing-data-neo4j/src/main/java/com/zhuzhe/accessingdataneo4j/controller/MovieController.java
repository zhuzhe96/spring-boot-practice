package com.zhuzhe.accessingdataneo4j.controller;

import com.zhuzhe.accessingdataneo4j.service.MovieService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movie")
public class MovieController {
  @Autowired
  private MovieService service;

  @GetMapping
  public ResponseEntity<?> findAll() {
    return ResponseEntity.of(Optional.ofNullable(service.findAll()));
  }

  @GetMapping("count")
  public ResponseEntity<?> count(){
    return ResponseEntity.ok(service.count());
  }
}
