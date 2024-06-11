package com.zhuzhe.accessingdatajpa.service;

import com.zhuzhe.accessingdatajpa.domain.Device;
import com.zhuzhe.accessingdatajpa.domain.vo.DeviceVO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface DeviceService {
  List<Device> getList(Long userId);
  Device getLast();
  Page<Device> getPage(String sortField, Integer pageNo, Integer pageSize);
  void modifyUserId(Long devId, Long userId);
  void save(DeviceVO vo);
}
