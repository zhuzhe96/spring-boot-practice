package com.zhuzhe.messagingjms;

import com.zhuzhe.common.entity.SimpleMessage;
import com.zhuzhe.messagingjms.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Slf4j
@EnableJms
@SpringBootApplication
public class MessagingJmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingJmsApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(MessageProducer producer){
		return args -> {
			producer.sendMessage("zhuzhe-d", new SimpleMessage(123L,"zhuzhe-d","Hello JMS!"));
		};
	}
}
