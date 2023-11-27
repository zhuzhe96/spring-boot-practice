package com.zhuzhe.accessingdatamongodb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user")
public class User {
  private String id;
  private String name;
  private String sex;
  private Integer salary;
  private Integer age;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date birthday;
  private String address;
  private Status status;
  private String role;
  private List<Device> devices;
}
