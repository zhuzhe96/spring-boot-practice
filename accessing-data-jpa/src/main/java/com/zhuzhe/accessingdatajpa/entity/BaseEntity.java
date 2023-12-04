package com.zhuzhe.accessingdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 实体父类
 */
@Data
@MappedSuperclass // 用在父类上面。当这个类肯定是父类时，加此标注。如果改成@Entity，则继承后，多个类继承，只会生成一个表，而不是多个继承，生成多个表
public abstract class BaseEntity implements Serializable {
  @Serial
  private static final long serialVersionUID = -4336519258736806392L;
  @Id
  @Column(name = "id",
      unique = true // 是唯一标识
  )
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @CreationTimestamp
  @Column(name = "create",
      nullable = false, // 不能为null
      updatable = false, // 不用参与数据更新
      columnDefinition = "datetime default CURRENT_TIMESTAMP"
  )
  private LocalDateTime create;
  @Column(name = "update",
      nullable = false,
      columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
  )
  private LocalDateTime update;
  @Version
  private Long version;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseEntity that = (BaseEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version);
  }
}
