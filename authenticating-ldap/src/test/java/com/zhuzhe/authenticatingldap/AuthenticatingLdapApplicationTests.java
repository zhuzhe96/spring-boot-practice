package com.zhuzhe.authenticatingldap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

import com.zhuzhe.authenticatingldap.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;



@SpringBootTest
@AutoConfigureMockMvc
@Import(WebSecurityConfig.class)
class AuthenticatingLdapApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void contextLoads() throws Exception {
    FormLoginRequestBuilder login = formLogin()
        .user("ben")
        .password("benspassword");
    // 模拟HTTP请求表单
    mockMvc.perform(login)
        .andExpect(authenticated().withUsername("ben"));
  }

}
