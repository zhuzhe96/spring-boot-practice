package com.zhuzhe.accessingdatajpa.service;

import com.zhuzhe.accessingdatajpa.domain.Device;
import java.util.List;
import org.springframework.data.domain.Page;

public interface DeviceService {
  List<Device> getList(Long userId);
  Device getLast();
  Page<Device> getPage(Integer pageNo, Integer pageSize);
}
