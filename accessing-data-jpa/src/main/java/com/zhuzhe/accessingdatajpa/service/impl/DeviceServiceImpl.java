package com.zhuzhe.accessingdatajpa.service.impl;

import com.zhuzhe.accessingdatajpa.entity.Device;
import com.zhuzhe.accessingdatajpa.repository.DeviceRepository;
import com.zhuzhe.accessingdatajpa.service.DeviceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
  private final DeviceRepository deviceRepository;

  @Override
  public List<Device> getList(Long userId) {
    return deviceRepository.findByUserId(userId);
  }
}
