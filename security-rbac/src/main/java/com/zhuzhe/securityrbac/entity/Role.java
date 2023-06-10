package com.zhuzhe.securityrbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class Role extends BaseEntity{

  @TableField("`name`")
  private String name;
  @TableField("`key`")
  private String key;
  @TableField("`desc`")
  private String desc;
  @TableField("`status`")
  private Integer status;
}
