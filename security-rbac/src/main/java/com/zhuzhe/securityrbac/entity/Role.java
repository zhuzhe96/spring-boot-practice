package com.zhuzhe.securityrbac.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity{
  private String name;
  private String desc;
  private Integer status;
  private String url;
  private String type;
  /**
   * 权限表达式: ${大模块}:${小模块}:{操作}
   * 超级管理员：*:*:*
   * 拥有小模块的所有操作权限：sys:user:*
   * 拥有多种权限时逗号隔开: sys:user:query,sys:user:add
   */
  private String permission;
}
