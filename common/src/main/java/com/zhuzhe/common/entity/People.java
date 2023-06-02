package com.zhuzhe.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class People {
  private Long id;
  private String name;
  private String email;
}
