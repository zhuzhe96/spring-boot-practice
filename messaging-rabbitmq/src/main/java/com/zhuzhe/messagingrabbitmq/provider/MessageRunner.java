package com.zhuzhe.messagingrabbitmq.provider;

import com.zhuzhe.common.entity.LogInfo;
import com.zhuzhe.messagingrabbitmq.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MessageRunner implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(MessageRunner.class);
  private final RabbitTemplate rabbitTemplate;

  public MessageRunner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args){
    log.info("消息发送");
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    rabbitTemplate.convertAndSend(RabbitMQConfig.LOG_EXCHANGE, RabbitMQConfig.LOG_ROUTING_KEY, new LogInfo("INFO","AAAAAAAAA"));
    rabbitTemplate.convertAndSend(RabbitMQConfig.LOG_EXCHANGE, RabbitMQConfig.LOG_ROUTING_KEY, new LogInfo("WARN","BBBBBBBBB"));
    rabbitTemplate.convertAndSend(RabbitMQConfig.LOG_EXCHANGE, RabbitMQConfig.LOG_ROUTING_KEY, new LogInfo("ERROR","CCCCCCCCC"));
  }
}
