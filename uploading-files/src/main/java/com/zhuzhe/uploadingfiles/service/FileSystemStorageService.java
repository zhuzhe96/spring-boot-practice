package com.zhuzhe.uploadingfiles.service;

import com.zhuzhe.common.exception.StorageException;
import com.zhuzhe.common.exception.StorageFileNotFoundException;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/*文件上传服务: 正常来说这种公用的东西是放在common中的,为了避免这个模块没东西,就先放部分在这*/
@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  /*获取指定存储路径*/
  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  /*根据路径创建存储文件夹*/
  @Override
  public void init() {
    try {
      Files.createDirectory(rootLocation);
    } catch (IOException e) {
      throw new StorageException("初始化文件存储路径失败", e);
    }
  }

  /*校验并上传文件*/
  @Override
  public void store(MultipartFile file) {
    try {
      if (StringUtils.isBlank(file.getOriginalFilename())) {
        throw new StorageException("文件名为空,无法上传");
      }
      if (file.isEmpty()) {
        throw new StorageException("文件[" +file.getOriginalFilename() + "]内容为空,无法上传");
      }
      // 将文件名和目录路径合并为一个完整的绝对路径
      var destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize()
          .toAbsolutePath();
      if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){
        throw new StorageException("无法将文件存储于指定目录之外");
      }
      // 存储文件,如果存在则替换
      try (InputStream inputStream = file.getInputStream()){
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("上传[" + file.getOriginalFilename() + "]失败,异常信息: ",e);
    }
  }

  /*获取指定根路径下的所有直接子目录(深度1)*/
  @SuppressWarnings("resource")
  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
          .map(path -> this.rootLocation.relativize(path));
    } catch (IOException e) {
      throw new StorageException("文件读取失败",e);
    }
  }

  /*将这个文件名与目录路径合并起来，形成一个新的 Path 对象。*/
  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  /*根据文件名,获得资源对象*/
  @Override
  public Resource loadAsResource(String filename) {
    try {
      var file = load(filename);
      var resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()){
        return resource;
      }else {
        throw new StorageFileNotFoundException("无法读取文件["+filename+"].");
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("无法读取文件["+filename+"], 异常信息: ",e);
    }
  }

  /*删除所有文件*/
  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }
}
