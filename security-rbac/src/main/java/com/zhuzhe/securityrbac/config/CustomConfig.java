package com.zhuzhe.securityrbac.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.io.Serializable;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableCaching//将方法返回值缓存起来
@AutoConfigureAfter(RedisAutoConfiguration.class)//先加载Redis的配置再加载当前的配置
@EnableConfigurationProperties(JwtConfig.class)//启动JwtConfig读取配置文件
public class CustomConfig {
  @Autowired
  private JwtConfig jwtConfig;

  /*Redis值序列化方式*/
  @Bean
  public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory factory){
    var template = new RedisTemplate<String, Serializable>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(factory);
    return template;
  }

  //生成签名
  @Bean(name = "signatureKey")
  public SecretKey secretKey(){
    var signature = jwtConfig.getSignature();
    var decode = Decoders.BASE64.decode(signature);
    return Keys.hmacShaKeyFor(decode);
  }

  //加密工具
  @Bean
  public BCryptPasswordEncoder encoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CommandLineRunner commandLineRunner(){
    return args -> log.info("应用启动...");
  }
}
