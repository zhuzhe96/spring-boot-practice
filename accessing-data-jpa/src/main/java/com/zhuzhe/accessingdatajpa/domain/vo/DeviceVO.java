package com.zhuzhe.accessingdatajpa.domain.vo;

import com.zhuzhe.accessingdatajpa.domain.Device;

public record DeviceVO(Long id, String mac, String sn, Boolean online, Boolean active, Long userId) {

  /**
   * VO -> PO
   */
  public Device toPersistentObject() {
    return Device.builder()
        .id(this.id)
        .mac(this.mac)
        .sn(this.sn)
        .online(this.online)
        .active(this.active)
        .userId(this.userId)
        .build();
  }
}
