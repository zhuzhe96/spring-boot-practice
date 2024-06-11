package com.zhuzhe.accessingdatajpa.domain;


import com.zhuzhe.accessingdatajpa.domain.vo.DeviceVO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
@EqualsAndHashCode(callSuper = true)
public class Device extends BaseEntity  {
  private String mac;
  private String sn;
  private Boolean online;
  private Boolean active;
  private Long userId;

  /**
   * PO -> VO
   */
  public DeviceVO toViewObject() {
    return new DeviceVO(super.id, this.mac, this.sn, this.online, this.active, this.userId);
  }
}
