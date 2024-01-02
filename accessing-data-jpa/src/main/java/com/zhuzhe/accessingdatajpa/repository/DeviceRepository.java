package com.zhuzhe.accessingdatajpa.repository;

import com.zhuzhe.accessingdatajpa.entity.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
  List<Device> findByUserId(Long userId);
}
