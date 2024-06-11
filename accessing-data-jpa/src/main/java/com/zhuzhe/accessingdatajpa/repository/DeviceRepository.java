package com.zhuzhe.accessingdatajpa.repository;

import com.zhuzhe.accessingdatajpa.domain.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

  /**
   * JPA查询规则
   */
  List<Device> findByUserId(Long userId);

  /**
   * JPQL语法
   * <p>等同 @Query(nativeQuery = true, value = "select * from device order by id desc limit 1;")</p>
   */
  @Query("select dev from Device dev order by dev.id desc limit 1")
  Device getLast();
}
