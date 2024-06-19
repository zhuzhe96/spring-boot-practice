package com.zhuzhe.accessingdatajpa.listener;

import com.zhuzhe.accessingdatajpa.domain.BaseEntity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomizeListener {
  private final String LOG_PREFIX = "[JPA持久化监听]: ";

  @PrePersist
  public void prePersist(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "新增操作之前", entity.getId());
  }

  @PostPersist
  public void postPersist(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "新增操作之后", entity.getId());
  }

  @PreUpdate
  public void preUpdate(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "更新操作之前", entity.getId());
  }

  @PostUpdate
  public void postUpdate(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "更新操作之后", entity.getId());
  }

  @PreRemove
  public void preRemove(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "删除操作之前", entity.getId());
  }

  @PostRemove
  public void postRemove(final BaseEntity entity) {
    log.info("{} [{}], id={}", LOG_PREFIX, "删除操作之后", entity.getId());
  }
}
