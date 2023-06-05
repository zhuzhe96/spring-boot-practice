package com.zhuzhe.batchprocessing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhuzhe.batchprocessing.annotation.ExcelColumn;
import com.zhuzhe.batchprocessing.annotation.ExcelTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ExcelTable(name = "人员表")
public class People {
  @TableId(type = IdType.AUTO)
  private Long id;
  @ExcelColumn(header = "姓名", width = 11)
  private String name;
  @ExcelColumn(header = "邮箱")
  private String email;
}
