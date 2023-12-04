package com.zhuzhe.accessingdatamongodb.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "device")
public class Device {
  @Id
  String id;
  String mac;
  String sn;
  String type;
  String name;
  String swr;
  String hw;
  boolean online;
  boolean active;
  String userId;
  String groupId;
  @CreatedDate
  private Long create;
  @LastModifiedDate
  private Long update;
  @Version
  private Long version;
  private String role;
  private User user;
}
