package com.zhuzhe.batchprocessing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhuzhe.batchprocessing.annotation.ExcelColumn;
import com.zhuzhe.batchprocessing.annotation.ExcelTable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelTable(name = "权限表")
@TableName("permission")
public class Permission {
  @TableId(type = IdType.AUTO)
  private Long id;
  @ExcelColumn(header = "权限名")
  private String name;
  @ExcelColumn(header = "父id", width = 7)
  private Long pid;
  @ExcelColumn(header = "权限链", width = 16)
  private String url;
  @ExcelColumn(header = "权限值", width = 16)
  private String code;
  @ExcelColumn(header = "数据创建时间", width = 16)
  private Date createTime;
  @ExcelColumn(header = "数据更新时间", width = 16)
  private Date updateTime;
}
