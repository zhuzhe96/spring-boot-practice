package com.zhuzhe.accessingdatajpa.repository;

import com.querydsl.core.types.EntityPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 持久层封装接口
 * @param <E> Entity, 业务实体
 * @param <Q> Query, 查询实体
 */
@NoRepositoryBean
public interface BaseJpaRepository<E, Q extends EntityPath<E>>
    extends JpaRepository<E, Long>, // 声明为Spring Data JPA的接口, <E>是实体类, Long是主键
    JpaSpecificationExecutor<E>,    // 配合JpaRepository使用，提供多条件查询，分页，排序功能
    QuerydslPredicateExecutor<E>,   // 增加使用QueryDSL语言构造查询
    QuerydslBinderCustomizer<Q> {   // 增加自定义QueryDSL的绑定行为
}
