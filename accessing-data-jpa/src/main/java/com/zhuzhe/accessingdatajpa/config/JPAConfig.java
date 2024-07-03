package com.zhuzhe.accessingdatajpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JPAConfig {
  @Bean
  public EntityCreateAuditor getEntityCreateAuditor() {
    return new EntityCreateAuditor();
  }
}
