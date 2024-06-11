package com.zhuzhe.accessingdatajpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 * 实体父类
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
  @Serial
  private static final long serialVersionUID = -4336519258736806392L;
  /**主键ID*/
  @Id
  @Column(name = "id", unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public BaseEntity() {}

  public BaseEntity(Long id) {
    this.id = id;
  }
}
