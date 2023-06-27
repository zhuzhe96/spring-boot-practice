package com.zhuzhe.accessingdatageode.repository;

import com.zhuzhe.accessingdatageode.entity.Person;
import org.springframework.data.gemfire.repository.query.annotation.Trace;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
  /*根据名称查询*/
  @Trace
  Person findByName(String name);
  /*查询年龄大于指定值的数据*/
  @Trace
  Iterable<Person> findByAgeGreaterThan(int age);
  /*查询年龄小雨指定值的数据*/
  @Trace
  Iterable<Person> findByAgeLessThan(int age);
  /*查询年龄在指定范围内的数据*/
  @Trace
  Iterable<Person> findByAgeGreaterThanAndAgeLessThan(int greaterThanAge, int lessThanAge);
}
