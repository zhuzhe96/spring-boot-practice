package com.zhuzhe.securityrbac.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity{
  private String name;
  private String url;
  private String type;
  private Long pid;
}
