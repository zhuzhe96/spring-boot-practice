package com.zhuzhe.h2service.entity;

import java.util.Objects;

public class QuoteResource {
  private String type;
  private Quote value;

  public QuoteResource(String type, Quote value) {
    this.type = type;
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Quote getValue() {
    return value;
  }

  public void setValue(Quote value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuoteResource that = (QuoteResource) o;
    return Objects.equals(type, that.type) && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public String toString() {
    return "QuoteResource{" +
        "type='" + type + '\'' +
        ", value=" + value +
        '}';
  }
}
