package com.zhuzhe.accessingdatajpa.domain.dto;

import com.zhuzhe.accessingdatajpa.domain.po.Device;
import lombok.Data;

@Data
public class DeviceDTO {
  private Long id;
  private String mac;
  private String sn;
  private Boolean online;
  private Boolean active;
  private Long userId;

  /**
   * DTO -> PO
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
