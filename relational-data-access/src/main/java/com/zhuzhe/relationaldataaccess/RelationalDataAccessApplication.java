package com.zhuzhe.relationaldataaccess;

import com.zhuzhe.common.entity.Customer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);

  @Autowired
  JdbcTemplate jdbcTemplate;

  public static void main(String[] args) {
    SpringApplication.run(RelationalDataAccessApplication.class, args);
  }


  @Override
  public void run(String... args) throws Exception {
    log.info("开始建表...");
    jdbcTemplate.execute("drop table customers if exists");
    jdbcTemplate.execute(
        "create table customers(" + "id serial, first_name varchar(255), last_name varchar(255))");

    log.info("初始化数据...");
    List<Object[]> splitUpNames = Arrays.asList("Zhu Zhe", "John Woo", "Jeff Dean", "Josh Bloch")
        .stream().map(name -> name.split(" ")).collect(Collectors.toList());
    splitUpNames.forEach(
        name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));
    jdbcTemplate.batchUpdate("insert into customers (first_name, last_name) values (?,?)",
        splitUpNames);

    log.info("查询数据库中名字：'Zhu'");
    jdbcTemplate.query("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
        new Object[]{"Zhu"},
        (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
            rs.getString("last_name"))).forEach(customer -> log.info(customer.toString()));
  }
}
