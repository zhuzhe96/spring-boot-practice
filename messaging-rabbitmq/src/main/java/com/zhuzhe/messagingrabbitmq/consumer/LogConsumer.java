package com.zhuzhe.messagingrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.zhuzhe.common.entity.LogInfo;
import com.zhuzhe.messagingrabbitmq.config.RabbitMQConfig;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {

  private static final Logger log = LoggerFactory.getLogger(LogConsumer.class);

  @RabbitListener(queues = RabbitMQConfig.LOG_QUEUE)
  @Retryable(value = {
      Exception.class}, maxAttempts = RabbitMQConfig.DEFAULT_MAX_ATTEMPTS, backoff = @Backoff(delay = 1000L))
  public void consumeMessage(LogInfo content, Message message, Channel channel) throws Exception {

    int retryCount = RetrySynchronizationManager.getContext().getRetryCount();
    log.info("content={} , 执行次数: {} of {}",content , retryCount+1, RabbitMQConfig.DEFAULT_MAX_ATTEMPTS);

    if (retryCount+1 == RabbitMQConfig.DEFAULT_MAX_ATTEMPTS){
      log.info("达到最大执行次数,消息拒绝,将进入死信队列");
      channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
      return;
    }
    processMessage(content);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
  }

  private void processMessage(LogInfo content) {
    log.info("content: {}", content);
  }

  @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
  public void consumeDeadMessage(LogInfo content, Message message, Channel channel){
    try {
      log.info("开始处理死信: {}",content);
      processDeadMessage(content);
      channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (Exception e) {
      log.error("死信队列处理失败: message={} , error={}, 直接丢弃", message, e);
      try {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private void processDeadMessage(LogInfo content) {
    log.info("content: {}", content);
  }
}
