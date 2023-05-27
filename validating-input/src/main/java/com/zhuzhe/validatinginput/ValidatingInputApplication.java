package com.zhuzhe.validatinginput;

import com.zhuzhe.common.annotation.GlobalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@GlobalConfiguration
public class ValidatingInputApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidatingInputApplication.class, args);
	}

}
