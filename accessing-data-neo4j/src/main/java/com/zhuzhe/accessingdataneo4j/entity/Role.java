package com.zhuzhe.accessingdataneo4j.entity;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@NoArgsConstructor
@RelationshipProperties
public class Role {
  @Id
  @GeneratedValue
  private Long id;
  // 演员的角色列表
  @Property("roles")
  private List<String> roles;
  // 在关系中被指向的节点, 也就是演员
  @TargetNode
  private Person person;

  public Role(List<String> roles, Person person) {
    this.roles = roles;
    this.person = person;
  }
}
