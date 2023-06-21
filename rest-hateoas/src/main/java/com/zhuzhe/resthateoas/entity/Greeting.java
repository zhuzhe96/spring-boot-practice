package com.zhuzhe.resthateoas.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

/**
 * 超媒体表述模型
 * 描述一些只包含链接信息或其他与超媒体相关的元数据的资源表述
 * 继承了RepresentationModel<Greeting>，表示该资源表述具有超媒体语义
 */
public class Greeting extends RepresentationModel<Greeting> {
  private final String content;

  @JsonCreator
  public Greeting(@JsonProperty("content") String content) {
    this.content = content;
  }

  @SuppressWarnings("unused")
  public String getContent(){
    return content;
  }
}
