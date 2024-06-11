package com.zhuzhe.accessingdatajpa.repository;

import com.zhuzhe.accessingdatajpa.domain.po.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

  /**
   * 根据用户id查询设备信息(JPA查询规则)
   * @param userId 要查询的用户id
   * <p>等同 @Query("select dev from Device dev where dev.userId = ?1")</p>
   */
  @Transactional(timeout = 10)
  List<Device> findByUserId(Long userId);

  /**
   * 查询最后一条设备信息(JPQL语法)
   * <p>等同 @Query(nativeQuery = true, value = "select * from device order by id desc limit 1;")</p>
   */
  @Query("select dev from Device dev order by dev.id desc limit 1")
  Device getLast();

  /**
   * 根据id更改设备信息中的用户id
   * @param userId 要更改的新用户id值
   * @param devId 要更改的设备id
   * @return 返回成功操作的条数
   */
  @Modifying
  @Transactional
  @Query("update Device dev set dev.userId = ?1, dev.updateTime = local_datetime where dev.id = ?2")
  Integer modifyUserIdById(Long userId, Long devId);
}
