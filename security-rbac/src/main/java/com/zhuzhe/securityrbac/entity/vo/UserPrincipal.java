package com.zhuzhe.securityrbac.entity.vo;

import com.zhuzhe.securityrbac.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;
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
public class UserPrincipal extends BaseEntity implements Serializable {
  private String username;
  private String password;
  private String nickname;
  private Date loginTime;
  private Long expireTime;
  private String address;
  private String browser;
}
