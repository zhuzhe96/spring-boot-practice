package com.zhuzhe.resthateoas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.zhuzhe.resthateoas.entity.Greeting;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
  public static final String TEMPLATE = "Hello, %s!";

  @RequestMapping("/greeting")
  public HttpEntity<Greeting> greeting(@RequestParam(value = "name", defaultValue = "zhuzhe") String name){
    var greeting = new Greeting(String.format(TEMPLATE, name));
    greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
    return new ResponseEntity<>(greeting, HttpStatus.OK);
  }
}
