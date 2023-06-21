package com.zhuzhe.resthateoas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.zhuzhe.resthateoas.entity.Book;
import com.zhuzhe.resthateoas.entity.Books;
import com.zhuzhe.resthateoas.entity.Greeting;
import com.zhuzhe.resthateoas.entity.Student;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HateoasController {
  public static final String TEMPLATE = "Hello, %s!";

  /*访问：超媒体表述模型*/
  @RequestMapping("/greeting")
  public HttpEntity<Greeting> greeting(
      @RequestParam(value = "name", defaultValue = "zhuzhe") String name) {
    // 通过调用add()方法来添加一个链接对象
    var greeting = new Greeting(String.format(TEMPLATE, name));
    greeting.add(linkTo(methodOn(HateoasController.class).greeting(name)).withSelfRel());
    return new ResponseEntity<>(greeting, HttpStatus.OK);
  }

  /*访问：实体资源模型*/
  @GetMapping("/student/{id}")
  public HttpEntity<Student> getStudent(@PathVariable Long id) {
    var student = new Student(id, "zhuzhe");
    student.add(linkTo(methodOn(HateoasController.class).getStudent(id)).withSelfRel());
    student.add(linkTo(methodOn(HateoasController.class).greeting("ccc")).withRel("greeting"));
    return new ResponseEntity<>(student, HttpStatus.OK);
  }

  /*访问：集合资源模型*/
  @GetMapping("/books")
  public ResponseEntity<?> getBooks() {
    var books = new Books(List.of(new Book(1L, "Java"), new Book(1L, "MySQL"), new Book(1L, "Html")));
    books.add(linkTo(HateoasController.class).withSelfRel());
    return ResponseEntity.ok(books);
  }
}
