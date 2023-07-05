package com.zhuzhe.accessingdatamongodb.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Status {
  private Integer weight;
  private Integer height;
}
