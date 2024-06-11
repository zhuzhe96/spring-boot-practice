package com.zhuzhe.accessingdatajpa.service;

import com.zhuzhe.accessingdatajpa.domain.dto.DeviceDTO;
import com.zhuzhe.accessingdatajpa.domain.vo.DeviceVO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface DeviceService {
  List<DeviceVO> getList(Long userId);
  DeviceVO getLast();
  Page<DeviceVO> getPage(String sortField, Integer pageNo, Integer pageSize);
  void modifyUserId(Long devId, Long userId);
  void save(DeviceDTO dto);
}
