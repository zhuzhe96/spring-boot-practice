package com.zhuzhe.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class MessagingRedisApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRedisApplication.class);

  /*连接工厂*/
  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter){
    var container = new RedisMessageListenerContainer();
    // 设置连接工厂
    container.setConnectionFactory(factory);
    // 设置监听器监听 chat 主题
    container.addMessageListener(adapter, new PatternTopic("chat"));
    return container;
  }

  /*消息监听容器*/
  @Bean
  MessageListenerAdapter adapter(Receiver receiver){
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  /*消息接收器*/
  @Bean
  Receiver receiver(){
    return new Receiver();
  }

  /*Redis 模板*/
  @Bean
  StringRedisTemplate template(RedisConnectionFactory factory){
    return new StringRedisTemplate(factory);
  }

  /*Spring容器启动和消息发送*/
  public static void main(String[] args) throws InterruptedException {
    var ctx = SpringApplication.run(MessagingRedisApplication.class, args);
    var template = ctx.getBean(StringRedisTemplate.class);
    var receiver = ctx.getBean(Receiver.class);
    while (receiver.getCount() == 0){
      LOGGER.info("Sending message...");
      template.convertAndSend("chat", "Hello from Redis.");
      Thread.sleep(500L);
    }
    System.exit(0);
  }
}
