package com.zhuzhe.accessingdatajpa.service;

import com.zhuzhe.accessingdatajpa.domain.vo.DeviceVO;
import java.util.List;

public interface DeviceService {
  List<DeviceVO> getList(String key, String sort, Integer page, Integer size);
}
