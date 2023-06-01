package com.zhuzhe.messagingjms.producer;

import com.zhuzhe.common.entity.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
  @Autowired
  private JmsTemplate jmsTemplate;

  public void sendMessage(String destinationName,  SimpleMessage message){
    jmsTemplate.convertAndSend(destinationName, message);
  }
}
