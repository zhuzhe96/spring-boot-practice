package com.zhuzhe.accessingdatajpa.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<E, Q extends EntityPath<E>>
    extends JpaRepository<E, Long>, // 声明为Spring Data JPA的接口, <E>是实体类, Long是主键
    JpaSpecificationExecutor<E>,    // 配合JpaRepository使用，提供多条件查询，分页，排序功能
    QuerydslPredicateExecutor<E>,   // 增加使用QueryDSL语言构造查询
    QuerydslBinderCustomizer<Q> {   // 增加自定义QueryDSL的绑定行为

  /**
   * 自定义QueryDSL的绑定行为
   * @param bindings 绑定行为
   * @param root Q类
   */
  @Override
  default void customize(QuerydslBindings bindings, Q root) {
    bindings
        // 对所有String类型成员，查询时不区分大小写匹配，则会加入到sql查询中
        .bind(String.class)
        .first((StringPath path, String value) -> path.containsIgnoreCase(value));
  }
}
