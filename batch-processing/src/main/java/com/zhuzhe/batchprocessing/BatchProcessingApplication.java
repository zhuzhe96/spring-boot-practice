package com.zhuzhe.batchprocessing;

import com.zhuzhe.batchprocessing.mapper.PeopleMapper;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@MapperScan("com.zhuzhe.batchprocessing.mapper")
public class BatchProcessingApplication {
  @Resource
  private PeopleMapper userMapper;

  public static void main(String[] args) {
    SpringApplication.run(BatchProcessingApplication.class, args);
  }

  @Bean
  CommandLineRunner runner(DataSource dataSource){
    return args -> {
      log.info("获取的数据库连接为: {}",dataSource.getConnection());
      var peopleList = userMapper.selectList(null);
      log.info("peopleList={}", peopleList);
    };
  }
}
