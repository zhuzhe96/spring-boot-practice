package com.zhuzhe.accessingdataneo4j.repository;

import com.zhuzhe.accessingdataneo4j.entity.Role;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoleRepository extends Neo4jRepository<Role, Long> {

}
