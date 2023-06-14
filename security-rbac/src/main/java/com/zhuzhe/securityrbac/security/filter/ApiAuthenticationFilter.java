package com.zhuzhe.securityrbac.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.security.ApiAuthenticationToken;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.exception.SecurityException;
import com.zhuzhe.securityrbac.security.service.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*登录认证，校验表单然后提交给Manager去验证登录*/
@Slf4j
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Autowired
  private TokenService tokenService;

  public ApiAuthenticationFilter(AuthenticationManager authenticationManager,
      String loginProcessingUrl) {
    super(authenticationManager);
    setFilterProcessesUrl(loginProcessingUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    log.info("[ApiAuthenticationFilter]拦截uri={}",request.getRequestURI());
    // 校验登录请求只能POST方式
    if (!"POST".equals(request.getMethod())){
      throw new SecurityException(Status.REQUEST_NOT_MATCH);
    }

    var username = request.getParameter("username");
    var password = request.getParameter("password");
    log.info("[ApiAuthenticationFilter]username={},password={}",username, password);

    // 登录校验
    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
      var userPrincipal = UserPrincipal.builder().username(username).password(password).build();
      ApiAuthenticationToken apiAuthenticationToken = ApiAuthenticationToken.unauthenticated(userPrincipal);
      var authenticate = getAuthenticationManager().authenticate(apiAuthenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
      return authenticate;
    }else {
      throw new SecurityException(Status.USERNAME_PASSWORD_ERROR);
    }
  }

  /*校验成功时的处理: 发token给用户，将用户信息存储到Redis*/
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException {
    log.info("[ApiAuthenticationFilter]登陆成功,开始发放令牌");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ApiAuthenticationToken apiAuthenticationToken = (ApiAuthenticationToken)authentication;
    var jwt = tokenService.createJwt(apiAuthenticationToken.getUserPrincipal());
    new ObjectMapper().writeValue(response.getWriter(), new JwtVO(jwt));
  }

  /*校验失败时的处理*/
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException e)
      throws IOException, ServletException {
    log.info("[ApiAuthenticationFilter]登陆失败,清除SecurityContext");
    SecurityContextHolder.clearContext();
    super.unsuccessfulAuthentication(request, response, e);
    if (e instanceof UsernameNotFoundException){
      new ObjectMapper().writeValue(response.getWriter(), Status.ERROR.custStatusMsg("用户名不存在"));
    }else if (e instanceof LockedException){
      new ObjectMapper().writeValue(response.getWriter(), Status.ERROR.custStatusMsg("用户名被冻结"));
    }else if (e instanceof BadCredentialsException){
      new ObjectMapper().writeValue(response.getWriter(), Status.ERROR.custStatusMsg("用户名密码不正确"));
    }else {
      new ObjectMapper().writeValue(response.getWriter(), Status.ERROR.custStatusMsg("登陆失败"));
    }
  }

  record JwtVO(String token, String tokenType){
    public JwtVO(String token){
      this(token, "Bearer");
    }
  }
}
