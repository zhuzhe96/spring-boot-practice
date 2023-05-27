package com.zhuzhe.accessingdataneo4j.controller;

import com.zhuzhe.accessingdataneo4j.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("person")
public class PersonController {
  @Autowired
  private PersonService service;

  @GetMapping("{title}")
  public ResponseEntity<?> findByMovieTitle(@PathVariable("title") String movieTitle){
    return ResponseEntity.ok(service.findPersonByMovieTitle(movieTitle));
  }
}
