package com.zhuzhe.accessingdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Where(clause = "deleted = false")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "update user set deleted = true where id=?")
public class User extends BaseEntity{
  @Column(name = "`name`")
  private String name;
  @Column(name = "phone")
  private String phone;
  @Column(name = "deleted", nullable = false)
  private Boolean deleted = Boolean.FALSE;
}
