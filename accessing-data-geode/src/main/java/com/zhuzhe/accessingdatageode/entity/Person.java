package com.zhuzhe.accessingdatageode.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Data
@Region(value = "People")
public class Person {
  @Id
  private final String name;
  private final Integer age;
  
}
