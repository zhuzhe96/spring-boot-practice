package com.zhuzhe.securityrbac.config;

import com.zhuzhe.securityrbac.service.PermissionService;
import com.zhuzhe.securityrbac.service.RoleService;
import com.zhuzhe.securityrbac.service.UserService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.io.Serializable;
import java.util.Arrays;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)//执行在Redis配置之后
@EnableConfigurationProperties(JwtConfig.class)// 配置参数
@EnableCaching// 启用缓存
public class CustomConfig {
  @Autowired
  private JwtConfig jwtConfig;

  // HTTP的返回模板
  @Bean
  public RestTemplate restTemplate(){
    var template = new RestTemplate();
    var converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML,MediaType.TEXT_PLAIN));
    template.getMessageConverters().add(converter);
    return template;
  }

  // Redis模板的序列化方式
  @Bean
  public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory factory){
    var template = new RedisTemplate<String, Serializable>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(factory);
    return template;
  }

  // 加密方式
  @Bean(name = "signatureKey")
  public SecretKey secretKey(){
    var signature = jwtConfig.getSignature();
    var decode = Decoders.BASE64.decode(signature);
    return Keys.hmacShaKeyFor(decode);
  }

  // 用户密码加密方式
  @Bean
  public BCryptPasswordEncoder encoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommandLineRunner commandLineRunner(UserService userService, RoleService roleService, PermissionService permissionService){
    return args -> {
      log.info("userList = {}", userService.list());
      log.info("roleList = {}", roleService.list());
      log.info("permissionList = {}", permissionService.list());
    };
  }
}
