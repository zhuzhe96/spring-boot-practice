package com.zhuzhe.messagingrabbitmq.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class RabbitMQConfig {

  public static final String LOG_QUEUE = "log.queue";
  public static final String LOG_EXCHANGE = "log.exchange";
  public static final String LOG_ROUTING_KEY = "log.routing.key";
  public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
  public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
  public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter.routing.key";
  public static final int DEFAULT_MAX_ATTEMPTS = 4;

  // 连接配置
  @Bean
  public ConnectionFactory connectionFactory() {
    var factory = new CachingConnectionFactory();
    factory.setHost("localhost");
    factory.setPort(5672);
    factory.setVirtualHost("/");
    factory.setUsername("guest");
    factory.setPassword("guest");
    return factory;
  }

  // 消息序列化方式
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // 消息模板
  @Bean
  public RabbitTemplate rabbitTemplate() {
    var template = new RabbitTemplate(connectionFactory());
    template.setExchange(LOG_EXCHANGE);
    template.setRoutingKey(LOG_ROUTING_KEY);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }

  // 日志队列
  @Bean
  public Queue logQueue() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE); // 指定死信交换器
    args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY); // 指定死信路由键
    args.put("x-message-ttl", 30000); // 设置消息过期时间，单位为毫秒
    return new Queue(LOG_QUEUE, true, false, false, args);
  }

  // 配置死信队列
  @Bean
  public Queue deadLetterQueue() {
    return new Queue(DEAD_LETTER_QUEUE);
  }

  // 配置交换器
  @Bean
  public DirectExchange logExchange() {
    return new DirectExchange(LOG_EXCHANGE);
  }

  // 配置死信交换器
  @Bean
  public FanoutExchange deadLetterExchange() {
    return new FanoutExchange(DEAD_LETTER_EXCHANGE);
  }

  // 绑定日志队列到交换器，并指定路由键
  @Bean
  public Binding logBinding() {
    return BindingBuilder.bind(logQueue()).to(logExchange()).with(LOG_ROUTING_KEY);
  }

  // 绑定死信队列到死信交换器
  @Bean
  public Binding deadLetterBinding() {
    return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange());
  }

  // 配置连接工厂
  @Bean
  public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(new Jackson2JsonMessageConverter());
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    factory.setDefaultRequeueRejected(false);
    return factory;
  }
}
