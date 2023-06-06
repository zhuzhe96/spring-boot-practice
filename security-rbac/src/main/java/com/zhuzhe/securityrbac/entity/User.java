package com.zhuzhe.securityrbac.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
  private String username;
  private String password;
  private String nickname;
  private Integer status;
  private Set<Role> roles;
}
