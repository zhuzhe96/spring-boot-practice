package com.zhuzhe.securityrbac.entity.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements Serializable {
  private Long id;
  private String username;
  private String password;
  private String nickname;
  private Date createTime;
  private Date updateTime;
  private Date loginTime;
  private Long expireTime;
  private Set<String> roles;
}
