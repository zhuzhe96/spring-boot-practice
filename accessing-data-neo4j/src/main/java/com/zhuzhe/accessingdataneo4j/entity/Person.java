package com.zhuzhe.accessingdataneo4j.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Node("Person")
public class Person {
  @Id
  @GeneratedValue
  private UUID uuid;
  @Property
  private String name;
  @Property
  private Integer born;

  public Person(String name, Integer born) {
    this.name = name;
    this.born = born;
  }
}
