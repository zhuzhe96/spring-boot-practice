package com.zhuzhe.accessingdatajpa.controller;

import com.zhuzhe.accessingdatajpa.domain.dto.DeviceDTO;
import com.zhuzhe.accessingdatajpa.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("device")
public class DeviceController {
  private final DeviceService deviceService;

  @GetMapping("list/{userId:\\d+}")
  public ResponseEntity<?> getList(@PathVariable Long userId) {
    return ResponseEntity.ok(deviceService.getList(userId));
  }

  @GetMapping("last")
  public ResponseEntity<?> getLast() {
    return ResponseEntity.ok(deviceService.getLast());
  }

  @GetMapping("page")
  public ResponseEntity<?> getPage(
      @RequestParam(value = "sort", required = false) String sortField,
      @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
    return ResponseEntity.ok(deviceService.getPage(sortField, pageNo, pageSize));
  }

  @PutMapping("modifyUserId")
  public ResponseEntity<?> modifyUserId(@RequestBody DeviceDTO dto) {
    deviceService.modifyUserId(dto.getId(), dto.getUserId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("save")
  public ResponseEntity<?> save(@RequestBody DeviceDTO dto) {
    deviceService.save(dto);
    return ResponseEntity.ok().build();
  }
}
