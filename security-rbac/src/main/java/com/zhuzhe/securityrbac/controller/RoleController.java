package com.zhuzhe.securityrbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuzhe.securityrbac.common.ApiResponse;
import com.zhuzhe.securityrbac.entity.UserRole;
import com.zhuzhe.securityrbac.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController {
  @Autowired private UserRoleService userRoleService;

  @PostMapping("bind")
  public ResponseEntity<?> bind(@RequestBody UserRole userRole) {
    userRoleService.save(userRole);
    return ResponseEntity.ok(ApiResponse.ofSuccess());
  }

  @DeleteMapping("unbind")
  public ResponseEntity<?> unbind(@RequestBody UserRole userRole) {
    userRoleService.remove(
        new QueryWrapper<UserRole>()
            .eq("user_id", userRole.userId())
            .eq("role_id", userRole.roleId()));
    return ResponseEntity.ok(ApiResponse.ofSuccess());
  }
}
