package com.zhuzhe.accessingdataneo4j.entity;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Data
@NoArgsConstructor
@Node("Movie")
public class Movie {
  @Id
  @GeneratedValue
  private UUID uuid;
  @Property
  private String title;
  @Property
  private String description;
  // Movie->roles 由电影指向演员, 说明一个电影有多个演员
  @Relationship(type = "has_outgoing", direction = Direction.OUTGOING)
  private List<Role> roles;
  // Movie<-directors 由导演指向电影, 说明一个电影有多个导演
  @Relationship(type = "has_incoming", direction = Direction.INCOMING)
  private List<Person> directors;

  public Movie(String title, String description) {
    this.title = title;
    this.description = description;
  }
}
