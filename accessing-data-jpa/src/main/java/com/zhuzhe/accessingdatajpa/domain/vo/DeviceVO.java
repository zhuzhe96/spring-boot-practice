package com.zhuzhe.accessingdatajpa.domain.vo;

public record DeviceVO(Long id, String mac, String sn, Boolean online, Boolean active, Long userId) {
}
