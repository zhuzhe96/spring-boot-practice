package com.zhuzhe.messagingredis;

import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*Redis 消息接收器*/
public class Receiver {
  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

  private final AtomicInteger counter = new AtomicInteger();

  public void receiveMessage(String message){
    LOGGER.info("Received<" + message +">");
    counter.incrementAndGet();
  }

  public int getCount(){
    return counter.get();
  }
}
