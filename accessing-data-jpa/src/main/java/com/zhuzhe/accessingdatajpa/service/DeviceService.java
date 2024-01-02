package com.zhuzhe.accessingdatajpa.service;

import com.zhuzhe.accessingdatajpa.entity.Device;
import java.util.List;

public interface DeviceService {
  List<Device> getList(Long userId);
}
