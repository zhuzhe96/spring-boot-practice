package com.zhuzhe.securityrbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public abstract class BaseEntity implements Serializable {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Date createTime;
  private Date updateTime;
}
