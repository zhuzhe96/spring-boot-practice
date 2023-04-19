package com.zhuzhe.uploadingfiles;

import com.zhuzhe.common.exception.StorageFileNotFoundException;
import com.zhuzhe.uploadingfiles.service.StorageService;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;//指定Mock对象在被调用时应该返回什么结果
import static org.mockito.BDDMockito.then;//更加详细地描述Mock对象在被调用时的行为
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UploadingFilesApplicationTests {

  /*MockMvc是Spring MVC提供的一个用于测试Controller的类，它可以模拟HTTP请求并验证处理结果。*/
  @Autowired
  private MockMvc mvc;
  @MockBean
  private StorageService storageService;

  @Test
  public void shouldListAllFiles() throws Exception {
    // 使用BDDMockito定义当调用loadAll()时返回Stream.of(...)的内容
    given(this.storageService.loadAll()).willReturn(
        Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
    // 使用MockMvc测试请求
    // 1. 当请求"/"时应该返回200
    // 2. 并且响应模型中存在files属性,是一个字符串列表,包括了"http://localhost/files/second.txt"
    this.mvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("files",
        Matchers.contains("http://localhost/files/first.txt",
            "http://localhost/files/second.txt")));
  }

  @Test
  public void shouldSaveUploadedFile() throws Exception {
    // 模拟上传文件
    var multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",
        "Spring Framework".getBytes());
    // 测试请求 这里我们文件上传后是会重定向刷新
    this.mvc.perform(multipart("/").file(multipartFile)).andExpect(status().isMovedTemporarily())
        .andExpect(header().string("Location", "/"));
    then(this.storageService).should().store(multipartFile);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void should404WhenMissingFile() throws Exception {
    given(this.storageService.loadAsResource("test.txt")).willThrow(
        StorageFileNotFoundException.class);
    this.mvc.perform(get("/files/test.txt")).andExpect(status().isInternalServerError());
  }
}
