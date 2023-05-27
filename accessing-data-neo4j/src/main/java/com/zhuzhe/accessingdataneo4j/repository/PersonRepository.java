package com.zhuzhe.accessingdataneo4j.repository;

import com.zhuzhe.accessingdataneo4j.entity.Person;
import java.util.List;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, UUID> {

  @Query("""
    MATCH (m:Movie)-[:has_outgoing]->(p:Person)
    WHERE m.title = $title
    RETURN p
    """)
  List<Person> findByMovieTitle(String title);
}
