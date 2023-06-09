package com.zhuzhe.securityrbac.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.security.ApiAuthenticationManager;
import com.zhuzhe.securityrbac.security.filter.ApiAuthenticationFilter;
import com.zhuzhe.securityrbac.security.filter.JwtAuthenticationFilter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@Configuration
@EnableWebSecurity//开启Security功能
@EnableMethodSecurity//开启后端验证, 对所有执行方法判断权限
public class SecurityConfig {
  @Autowired
  private ApiAuthenticationManager apiAuthenticationManager;

  /*定义过滤链*/
  @Bean
  @SuppressWarnings("all")
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //登陆认证过滤器
    http.addFilter(apiAuthenticationFilter());

    //JWT认证过滤器, 在登陆认证过滤器之前执行
    http.addFilterBefore(jwtAuthenticationFilter(), ApiAuthenticationFilter.class);

    //  开启匿名认证
    http.anonymous();

    // 禁用Basic认证
    http.httpBasic().disable();

    // 验证路径(放行两个路径)
    http.authorizeHttpRequests().requestMatchers("/", "/user/register").permitAll().and()
        .authorizeHttpRequests().anyRequest().authenticated();

    // 设置session无状态
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 设置未授权请求异常处理
    http.exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler())
        .authenticationEntryPoint(authenticationEntryPoint()));

    //关闭跨域防护
    http.csrf().disable();

    return http.build();
  }

  /*登陆过滤器*/
  @Bean
  public ApiAuthenticationFilter apiAuthenticationFilter() {
    return new ApiAuthenticationFilter(apiAuthenticationManager, "/user/login");
  }

  /*JWT过滤器*/
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  /*权限异常：登录成功。但无权限*/
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      log.info("[AccessDeniedHandler]: 异常", accessDeniedException);
      new ObjectMapper().writeValue(response.getWriter(), Status.SC_ACCESS_DENIED);
    };
  }

  /*认证异常：登录失败。*/
  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      log.error("[authenticationEntryPoint]: 异常", authException);
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      response.setStatus(401);
      new ObjectMapper().writeValue(response.getWriter(), Map.of(Status.NOT_LOGIN.getCode(), Status.NOT_LOGIN.getMessage()));
    };
  }

  /*放行所有不需要登录就可以访问的请求*/
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(){
    return web -> web.ignoring().requestMatchers("/url1","/url2","/user/register");
  }
}
