package com.zhuzhe.batchprocessing.controller;

import com.zhuzhe.batchprocessing.entity.Permission;
import com.zhuzhe.batchprocessing.service.PeopleService;
import com.zhuzhe.batchprocessing.service.PermissionService;
import com.zhuzhe.batchprocessing.util.PoiUtils;
import com.zhuzhe.batchprocessing.annotation.ExcelTable;
import com.zhuzhe.batchprocessing.entity.People;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class TestController {

  @Autowired
  private PeopleService peopleService;
  @Autowired
  private PermissionService permissionService;

  @GetMapping("people/download")
  public void downloadPeopleExcel(HttpServletResponse response) {
    try {
      var workbook = PoiUtils.writeDataToExcel(peopleService.list());
      response.reset();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition",
          "attachment;filename=people_" + System.currentTimeMillis() + "."
              + People.class.getAnnotation(
              ExcelTable.class).format());
      OutputStream os = response.getOutputStream();
      workbook.write(os);
      workbook.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("people/upload")
  public ResponseEntity<?> uploadPeopleExcel(@RequestParam(value = "file") MultipartFile file) {
    try {
      List<People> list = PoiUtils.readDataFormExcel(file.getInputStream(), People.class);
      peopleService.saveBatch(list);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("permission/download")
  public void downloadPermissionExcel(HttpServletResponse response) {
    try {
      var workbook = PoiUtils.writeDataToExcel(permissionService.list());
      response.reset();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition",
          "attachment;filename=permission_" + System.currentTimeMillis() + "."
              + Permission.class.getAnnotation(
              ExcelTable.class).format());
      OutputStream os = response.getOutputStream();
      workbook.write(os);
      workbook.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("permission/upload")
  public ResponseEntity<?> uploadPermissionExcel(@RequestParam(value = "file") MultipartFile file) {
    try {
      List<Permission> list = PoiUtils.readDataFormExcel(file.getInputStream(), Permission.class);
      permissionService.saveBatch(list);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok().build();
  }
}
