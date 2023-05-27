package com.zhuzhe.accessingdataneo4j.service;

import com.zhuzhe.accessingdataneo4j.entity.Person;
import com.zhuzhe.accessingdataneo4j.repository.PersonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
  public final PersonRepository repository;

  public List<Person> findPersonByMovieTitle(String title){
    return repository.findByMovieTitle(title);
  }
}
