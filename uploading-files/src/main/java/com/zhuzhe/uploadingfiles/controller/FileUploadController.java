package com.zhuzhe.uploadingfiles.controller;

import com.zhuzhe.uploadingfiles.service.StorageService;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {
  private final StorageService storageService;
  @Autowired
  public FileUploadController(StorageService storageService) {
    this.storageService = storageService;
  }

  /*加载文件列表*/
  @GetMapping("/")
  public String listUploadedFiles(Model model) {
    //将所有文件和指定控制器构建成一个可访问URL
    model.addAttribute("files", storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(
        FileUploadController.class, "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(
        Collectors.toList()));
    return "uploadForm";
  }

  /*文件访问处理 xxx.xx*/
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename){
    //先拿文件名读到本地存储,构建Resource对象
    var file = storageService.loadAsResource(filename);
    //构建返回体,实现文件的下载
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }


  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
    //上传文件
    storageService.store(file);
    //回显结果
    redirectAttributes.addFlashAttribute("message", "You successfully uploaded "+file.getOriginalFilename()+"!");
    //重定向实现信息更新
    return "redirect:/";
  }
}
