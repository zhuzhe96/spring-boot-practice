package com.zhuzhe.securityrbac.controller;

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
  @Autowired
  private UserRoleService userRoleService;

  @PostMapping("bind")
  public ResponseEntity<?> bind(@RequestBody UserRole userRole){
    userRoleService.bindUserRole(userRole);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("unbind")
  public ResponseEntity<?> unbind(@RequestBody UserRole userRole){
    userRoleService.unbindUserRole(userRole);
    return ResponseEntity.ok(null);
  }
}
