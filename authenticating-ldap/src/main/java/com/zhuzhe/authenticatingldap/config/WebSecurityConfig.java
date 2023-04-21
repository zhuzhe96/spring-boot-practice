package com.zhuzhe.authenticatingldap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
    return http.build();
  }

  // 覆盖WebSecurityConfigurerAdapter，实现WebSecurityConfigurer接口方法，接入ldap处理
  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups")
        .contextSource().url("ldap://localhost:8389/dc=springframework,dc=org").and()
        .passwordCompare().passwordEncoder(new BCryptPasswordEncoder())
        .passwordAttribute("userPassword");
  }
}
