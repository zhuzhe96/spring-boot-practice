package com.zhuzhe.resthateoas.entity;

import java.util.List;
import org.springframework.hateoas.CollectionModel;

public class Books extends CollectionModel<Book> {
  public Books(List<Book> books) {
    super(books);
  }
}
