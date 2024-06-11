package com.zhuzhe.accessingdatajpa.service.impl;

import com.zhuzhe.accessingdatajpa.domain.Device;
import com.zhuzhe.accessingdatajpa.domain.vo.DeviceVO;
import com.zhuzhe.accessingdatajpa.repository.DeviceRepository;
import com.zhuzhe.accessingdatajpa.service.DeviceService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
  private final DeviceRepository deviceRepository;

  @Override
  public List<DeviceVO> getList(Long userId) {
    return deviceRepository.findByUserId(userId).stream().map(Device::toViewObject).collect(
        Collectors.toList());
  }

  @Override
  public DeviceVO getLast() {
    return deviceRepository.getLast().toViewObject();
  }

  @Override
  public Page<DeviceVO> getPage(String sortField,Integer pageNo, Integer pageSize) {
    var sort = Sort.by(Direction.DESC, sortField);
    var page = deviceRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
    return page.map(Device::toViewObject);
  }

  @Override
  public void modifyUserId(Long devId, Long userId) {
    var result = deviceRepository.modifyUserIdById(userId, devId);
    if (result == 0) {
      log.warn("更新失败, 条件有误: devId = {}, userId = {}", devId, userId);
    }
  }

  @Override
  public void save(DeviceVO vo) {
    deviceRepository.save(vo.toPersistentObject());
  }
}
