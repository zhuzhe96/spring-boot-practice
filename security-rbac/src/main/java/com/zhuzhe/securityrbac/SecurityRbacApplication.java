package com.zhuzhe.securityrbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhuzhe.securityrbac.mapper")
public class SecurityRbacApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityRbacApplication.class, args);
	}

}
