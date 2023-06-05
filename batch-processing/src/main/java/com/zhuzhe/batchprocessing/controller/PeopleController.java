package com.zhuzhe.batchprocessing.controller;

import com.zhuzhe.batchprocessing.service.PeopleService;
import com.zhuzhe.batchprocessing.util.PoiUtils;
import com.zhuzhe.batchprocessing.annotation.ExcelTable;
import com.zhuzhe.batchprocessing.entity.People;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
public class PeopleController {

  @Autowired
  private PeopleService service;

  @GetMapping("download")
  public void download(HttpServletResponse response) {
    try {
      var workbook = PoiUtils.writeDataToExcel(service.list());
      response.reset();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition",
          "attachment;filename=table_" + System.currentTimeMillis() + "."
              + People.class.getAnnotation(
              ExcelTable.class).format());
      OutputStream os = response.getOutputStream();
      workbook.write(os);
      workbook.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("upload")
  public ResponseEntity<?> upload(@RequestParam(value = "file") MultipartFile file) {
    try {
      var list = PoiUtils.readDataFormExcel(file.getInputStream());
      service.saveBatch(list);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok().build();
  }
}
