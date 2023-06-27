package com.zhuzhe.accessingdatageode;

import com.zhuzhe.accessingdatageode.entity.Person;
import com.zhuzhe.accessingdatageode.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication
@ClientCacheApplication
@EnableEntityDefinedRegions(
    basePackageClasses = Person.class,
    clientRegionShortcut = ClientRegionShortcut.LOCAL)
@EnableGemfireRepositories
public class AccessingDataGeodeApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccessingDataGeodeApplication.class, args);
  }

  @Bean
  CommandLineRunner runner(PersonRepository repository) {
    return args -> {
      var p1 = new Person("zhuzhe", 26);
      var p2 = new Person("cxr", 16);
      var p3 = new Person("cxk", 22);

      List<Person> list = new ArrayList<>(List.of(p1, p2, p3));
      repository.saveAll(list);
      System.out.println("list=" + repository.findAll());

      list.forEach(p -> System.out.println("person=" + repository.findByName(p.getName())));

      System.out.println("\n\nage > 16 : ");
      StreamSupport.stream(repository.findByAgeGreaterThan(16).spliterator(), false)
          .forEach(System.out::println);

      System.out.println("\n\nage < 26 : ");
      StreamSupport.stream(repository.findByAgeLessThan(26).spliterator(), false)
          .forEach(System.out::println);

      System.out.println("\n\nage > 16 and age < 26 : ");
      StreamSupport.stream(
              repository.findByAgeGreaterThanAndAgeLessThan(16, 26).spliterator(), false)
          .forEach(System.out::println);
    };
  }
}
