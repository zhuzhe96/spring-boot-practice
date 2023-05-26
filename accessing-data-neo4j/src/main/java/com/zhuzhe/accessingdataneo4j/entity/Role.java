package com.zhuzhe.accessingdataneo4j.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class Role {
  @RelationshipId
  private Long id;
  // 演员的角色列表
  @Property("roles")
  private List<String> roleList;
  // 在关系中被指向的节点, 也就是演员
  @TargetNode
  private Person person;
}
