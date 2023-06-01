package com.zhuzhe.common.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMessage implements Serializable {
  private Long id;
  private String source;
  private String content;
}
