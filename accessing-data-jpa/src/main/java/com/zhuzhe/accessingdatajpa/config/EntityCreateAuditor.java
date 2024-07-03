package com.zhuzhe.accessingdatajpa.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class EntityCreateAuditor implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of((Math.random() * 1000) + "");
  }
}
