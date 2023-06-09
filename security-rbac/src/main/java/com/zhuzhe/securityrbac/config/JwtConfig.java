package com.zhuzhe.securityrbac.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")//开启配置文件注入前缀
public class JwtConfig {
  /*公钥*/
  private String signature = "=M8d9QVZlDz9fbofoYjLEjvXWnIyiORN=FnHjp6z+42";
  /*有效期*/
  private Long ttl = 1000L * 60 * 60 * 24 * 7;
}
