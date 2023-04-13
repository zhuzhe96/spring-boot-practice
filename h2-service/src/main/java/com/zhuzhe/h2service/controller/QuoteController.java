package com.zhuzhe.h2service.controller;

import com.zhuzhe.h2service.entity.Quote;
import com.zhuzhe.h2service.entity.QuoteResource;
import com.zhuzhe.h2service.repository.QuoteRepository;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

  private static final Quote NONE = new Quote("Node");
  public static final Random RANDOMIZER = new Random();
  private final QuoteRepository repository;

  public QuoteController(QuoteRepository repository) {
    this.repository = repository;
  }

  /*查全部*/
  @GetMapping("/api")
  public List<QuoteResource> getAll() {
    return repository.findAll().stream().map(quote -> new QuoteResource("success", quote)).collect(
        Collectors.toList());
  }

  /*根据id查一个*/
  @GetMapping("/api/{id:\\d+}")
  public QuoteResource getOne(@PathVariable Long id) {
    return repository.findById(id).map(quote -> new QuoteResource("success", quote))
        .orElse(new QuoteResource("Quote " + id + " does not exist.", NONE));
  }

  /*在h2数据库id范围内进行随机返回*/
  @GetMapping("/api/random")
  public QuoteResource getRandomOne(){
    return getOne(nextLong(1, repository.count()+1));
  }

  private long nextLong(long lowerRange, long upperRange){
    return (long) (RANDOMIZER.nextDouble() * (upperRange - lowerRange)) + lowerRange;
  }
}
