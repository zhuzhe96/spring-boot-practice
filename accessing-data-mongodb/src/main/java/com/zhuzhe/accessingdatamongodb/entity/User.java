package com.zhuzhe.accessingdatamongodb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@ToString
@Accessors(chain = true)
public class User {
  @MongoId
  private String id;
  private String name;
  private String sex;
  private Integer salary;
  private Integer age;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date birthday;
  private String remake;
  private Status status;
}
