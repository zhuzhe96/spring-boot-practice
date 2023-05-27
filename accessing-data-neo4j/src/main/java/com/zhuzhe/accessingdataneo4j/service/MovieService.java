package com.zhuzhe.accessingdataneo4j.service;

import com.zhuzhe.accessingdataneo4j.entity.Movie;
import com.zhuzhe.accessingdataneo4j.repository.MovieRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
  private final MovieRepository repository;
  private final Neo4jTemplate template;

  public Movie add(Movie movie){
    return repository.save(movie);
  }

  public List<Movie> findAll(){
    return repository.findAll();
  }

  public Long count(){return template.count(Movie.class);}
}
