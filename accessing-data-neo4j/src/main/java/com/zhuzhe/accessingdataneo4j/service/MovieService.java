package com.zhuzhe.accessingdataneo4j.service;

import com.zhuzhe.accessingdataneo4j.entity.Movie;
import com.zhuzhe.accessingdataneo4j.repository.MovieRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
  private final MovieRepository repository;
  private final Neo4jTemplate neo4jTemplate;

  public Movie add(Movie movie){
    return repository.save(movie);
  }

  public List<Movie> findAll(){
    return repository.findAll();
  }

  public List<Movie> findByTitle(String movieTitle){
    return repository.findByTitle(movieTitle);
  }
  @SuppressWarnings("unused")
  public Movie findByTitle2(String movieTitle){
    return repository.findById(UUID.fromString(movieTitle)).orElse(null);
  }
  @SuppressWarnings("unused")
  public Movie findByTitle3(String movieTitle){
    return neo4jTemplate.findById(movieTitle,Movie.class).orElse(null);
  }
  @SuppressWarnings("unused")
  public Long count(){
    return repository.count();
  }

  public Long countAllNodes(){
    return repository.countAllNodes();
  }
  @SuppressWarnings("unused")
  public void deleteAll(Movie movie){
    if (Objects.isNull(movie)){
      repository.deleteAll();
    }else {
      repository.delete(movie);
    }
  }

  public List<Movie> findMovieDetails(){
    return repository.findMovieDetails();
  }
}
