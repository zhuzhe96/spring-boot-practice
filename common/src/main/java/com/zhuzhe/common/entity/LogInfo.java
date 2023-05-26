package com.zhuzhe.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LogInfo(String level, String content){
  @JsonCreator
  public LogInfo(@JsonProperty("level") String level,@JsonProperty("content") String content) {
    this.level = level;
    this.content = content;
  }
}
