package com.zhuzhe.securityrbac.security;

import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/*校验对象，用于登录认证*/
@Getter
@Setter
public class ApiAuthenticationToken extends AbstractAuthenticationToken {
  private UserPrincipal userPrincipal;

  /*创建一个未经验证的Token对象*/
  public ApiAuthenticationToken(UserPrincipal userPrincipal){
    super(null);
    this.userPrincipal = userPrincipal;
  }

  /*创建一个经过验证的Token对象*/
  public ApiAuthenticationToken(UserPrincipal userPrincipal,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.userPrincipal = userPrincipal;
    // 表明该对象已经通过身份验证并且在授权方面也是可信的
    super.setAuthenticated(true);
  }

  public static ApiAuthenticationToken unauthenticated(UserPrincipal userPrincipal){
    return new ApiAuthenticationToken(userPrincipal);
  }

  public static ApiAuthenticationToken authenticated(UserPrincipal userPrincipal, Collection<? extends GrantedAuthority> authorities){
    return new ApiAuthenticationToken(userPrincipal, authorities);
  }

  /*返回用户的凭证*/
  @Override
  public Object getCredentials() {
    return this.userPrincipal.getPassword();
  }

  /*返回用户的主体*/
  @Override
  public Object getPrincipal() {
    return this.userPrincipal.getUsername();
  }

  /*标记身份验证信息是否已经被验证过*/
  @Override
  public void setAuthenticated(boolean authenticated) {
    super.setAuthenticated(authenticated);
  }

  /*清除敏感信息*/
  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
  }
}
