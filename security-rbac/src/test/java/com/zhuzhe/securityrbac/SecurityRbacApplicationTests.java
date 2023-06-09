package com.zhuzhe.securityrbac;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
class SecurityRbacApplicationTests {
	@Autowired
	private PasswordEncoder encoder;

	@Test
	void contextLoads() {
		log.info("encodePassword={}",encoder.encode("123456"));
		log.info("match={}",encoder.matches("123456", "$2a$10$5AOohKh8QSGh2lCHHM2ZCOeHqIEFuIMgah7aZnTLnXRgSvT46hgL6"));
	}

}
