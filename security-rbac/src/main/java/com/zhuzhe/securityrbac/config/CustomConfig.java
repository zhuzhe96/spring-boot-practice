package com.zhuzhe.securityrbac.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.io.Serializable;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableCaching//将方法返回值缓存起来
@AutoConfigureAfter(RedisAutoConfiguration.class)//先加载Redis的配置再加载当前的配置
@EnableConfigurationProperties(JwtConfig.class)//启动JwtConfig读取配置文件
public class CustomConfig {
  @Autowired
  private JwtConfig jwtConfig;

  @Value("${security.ignored-urls}")
  private List<String> ignoredUrls;

  @Bean
  public List<String> ignoredUrls() {
    return ignoredUrls;
  }

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

  // json转换,不转换空值
  @Bean
  @Primary//首选bean
  @ConditionalOnMissingBean(ObjectMapper.class)// 当没有提供ObjectMapper时将会创建
  public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder){
    var objectMapper = builder.createXmlMapper(false).build();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper;
  }

  @Bean
  public CommandLineRunner commandLineRunner(){
    return args -> log.info("应用启动...");
  }
}
