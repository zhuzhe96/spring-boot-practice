package com.zhuzhe.securityrbac.config;

import com.zhuzhe.securityrbac.security.ApiAuthenticationManager;
import com.zhuzhe.securityrbac.security.filter.ApiAuthenticationFilter;
import com.zhuzhe.securityrbac.security.filter.JwtAuthenticationFilter;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@Configuration
@EnableWebSecurity// 开启Security功能
@EnableMethodSecurity// 开启后端验证, 对所有执行方法判断权限
public class SecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private ApiAuthenticationManager apiAuthenticationManager;

  /*定义过滤链*/
  @Bean
  @SuppressWarnings("all")
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 插入自定义过滤器
    http.addFilter(authenticationFilter());
    http.addFilterBefore(jwtAuthenticationFilter(), AuthenticationFilter.class);

    //  开启匿名认证
    http.anonymous();

    // 禁用Basic认证
    http.httpBasic().disable();

    // 验证路径 除了指定路径的访问放行,其他访问都得经过认证
    http.authorizeHttpRequests().requestMatchers("/", "/user/register").permitAll().and()
        .authorizeHttpRequests().anyRequest().authenticated();

    // 设置session无状态
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 设置未授权请求异常处理
    http.exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint()));

    //关闭跨域防护
    http.csrf().disable();

    return http.build();
  }

  @Bean
  public ApiAuthenticationFilter authenticationFilter(){
    var apiAuthenticationFilter = new ApiAuthenticationFilter(apiAuthenticationManager,
        "/user/login");
    //apiAuthenticationFilter.setAuthenticationSuccessHandler();
    return apiAuthenticationFilter;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(){
    return new JwtAuthenticationFilter();
  }

  /*登陆成功但是没有授权*/
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      log.error("[AccessDeniedHandler]: 异常",accessDeniedException);
      throw new RuntimeException("访问未授权!"+accessDeniedException.getMessage());
    };
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(){
    return (request, response, authException) -> {
      log.error("[authenticationEntryPoint]: 异常",authException);
      throw new RuntimeException("令牌已过期!"+authException.getMessage());
    };
  }
}
