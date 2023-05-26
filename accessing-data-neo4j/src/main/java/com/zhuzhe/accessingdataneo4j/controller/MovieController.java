package com.zhuzhe.accessingdataneo4j.controller;

import com.zhuzhe.accessingdataneo4j.entity.Movie;
import com.zhuzhe.accessingdataneo4j.service.MovieService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movie")
public class MovieController {
  @Autowired
  private MovieService movieService;

  @GetMapping
  public List<Movie> findAll() {
    return movieService.findAll();
  }

  @GetMapping("/findByTitle")
  public List<Movie> findByTitle(@RequestParam("title") String movieTitle) {
    return movieService.findByTitle(movieTitle);
  }

  @GetMapping("/allNodes")
  public Long countAllNodes() {
    return movieService.countAllNodes();
  }

  @GetMapping("details")
  public List<Movie> findMovieDetails(){
    return movieService.findMovieDetails();
  }
}
