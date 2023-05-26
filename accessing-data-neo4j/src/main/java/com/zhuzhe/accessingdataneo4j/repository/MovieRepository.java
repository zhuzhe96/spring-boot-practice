package com.zhuzhe.accessingdataneo4j.repository;

import com.zhuzhe.accessingdataneo4j.entity.Movie;
import java.util.List;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, UUID> {

  @Query("MATCH (n:Movie) where n.title contains $title RETURN n")
  List<Movie> findByTitle(@Param("title") String title);

  @Query("MATCH (n) RETURN count(n)")
  Long countAllNodes();

  @Query("""
          MATCH (m:Movie)-[:ACTED_IN]->(r:Role)-[:ACTOR]->(a:Person)
          WHERE m.title = '电影名称'
          RETURN a.name AS actor, r.roleList AS roles
      """)
  List<Movie> findMovieDetails();
}
