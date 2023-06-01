package com.zhuzhe.messagingjms.receiver;

import com.zhuzhe.common.entity.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Receiver {
  @JmsListener(destination = "zhuzhe-d")
  public void receiverMessage(SimpleMessage message){
    log.info("Receiver: {}",message);
  }
}
