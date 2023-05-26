package com.zhuzhe.accessingdataneo4j;

import com.zhuzhe.accessingdataneo4j.entity.Movie;
import com.zhuzhe.accessingdataneo4j.entity.Person;
import com.zhuzhe.accessingdataneo4j.entity.Role;
import com.zhuzhe.accessingdataneo4j.repository.MovieRepository;
import com.zhuzhe.accessingdataneo4j.repository.PersonRepository;
import com.zhuzhe.accessingdataneo4j.repository.RoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class AccessingDataNeo4jApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccessingDataNeo4jApplication.class, args);
  }

  @Bean
  CommandLineRunner initData(MovieRepository movieRepository, PersonRepository personRepository, RoleRepository roleRepository) {
    return args -> {
      // 因为是测试数据,所以每次启动时先清空后插入
      movieRepository.deleteAll();
      roleRepository.deleteAll();
      personRepository.deleteAll();
      Movie movie = new Movie("我爱我的祖国", "该片讲述了新中国成立70年间普通百姓与共和国息息相关的故事");
      List<Role> rolesList = new ArrayList<>(8);
      // 例: 电影中第一个演员是, 1974年出生的 黄渤 饰演的 林治远
      rolesList.add(new Role(1L, List.of("林治远"), new Person("黄渤", 1974)));
      rolesList.add(new Role(2L, List.of("高远"), new Person("张译", 1978)));
      rolesList.add(new Role(3L, List.of("陈冬冬"), new Person("吴京", 1974)));
      rolesList.add(new Role(4L, List.of("朱涛"), new Person("杜江", 1985)));
      rolesList.add(new Role(5L, List.of("张北京"), new Person("葛优", 1957)));
      rolesList.add(new Role(6L, List.of("沃德乐"), new Person("刘昊然", 1997)));
      movie.setRoles(rolesList);
      // 例: 电影中的其中一个导演是, 1952年出生的 陈凯歌
      movie.setDirectors(Collections.singletonList(new Person( "陈凯歌",1952)));
      movieRepository.save(movie);
    };
  }
}
