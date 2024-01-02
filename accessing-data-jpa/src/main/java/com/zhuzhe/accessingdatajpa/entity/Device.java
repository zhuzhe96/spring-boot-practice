package com.zhuzhe.accessingdatajpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "device")
@EqualsAndHashCode(callSuper = true)
public class Device extends BaseEntity  {
  @Column(name = "mac")
  private String mac;
  @Column(name = "sn")
  private String sn;
  @Column(name = "online")
  private Boolean online;
  @Column(name = "active")
  private Boolean active;
  @Column(name = "user_id")
  private Long userId;
  @Id
  private Long id;
}
