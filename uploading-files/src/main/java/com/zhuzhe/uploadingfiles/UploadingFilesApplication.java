package com.zhuzhe.uploadingfiles;

import com.zhuzhe.common.annotation.GlobalConfiguration;
import com.zhuzhe.uploadingfiles.service.StorageProperties;
import com.zhuzhe.uploadingfiles.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@GlobalConfiguration
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

  public static void main(String[] args) {
    SpringApplication.run(UploadingFilesApplication.class, args);
  }

  @Bean
  CommandLineRunner init(StorageService storageService){
    return args -> {
      /*每次程序启动时都清空重新初始化*/
      storageService.deleteAll();
      storageService.init();
    };
  }
}
