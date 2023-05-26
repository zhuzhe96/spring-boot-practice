package com.zhuzhe.accessingdataneo4j.repository;

import com.zhuzhe.accessingdataneo4j.entity.Person;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, UUID> {

}
