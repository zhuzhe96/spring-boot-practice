package com.zhuzhe.securityrbac.config;

import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class ApiAuthenticationToken extends AbstractAuthenticationToken {

  private UserPrincipal userPrincipal;

  public ApiAuthenticationToken(UserPrincipal userPrincipal){
    super(null);
    this.userPrincipal = userPrincipal;
  }

  public ApiAuthenticationToken(UserPrincipal userPrincipal,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.userPrincipal = userPrincipal;
    super.setAuthenticated(true);
  }

  /*未验证登录*/
  public static ApiAuthenticationToken unauthenticated(UserPrincipal userPrincipal){
    return new ApiAuthenticationToken(userPrincipal);
  }

  /*已验证登录*/
  public static ApiAuthenticationToken authenticated(UserPrincipal userPrincipal, Collection<? extends GrantedAuthority> authorities){
    return new ApiAuthenticationToken(userPrincipal, authorities);
  }

  @Override
  public Object getCredentials() {
    return this.userPrincipal.getPassword();
  }

  @Override
  public Object getPrincipal() {
    return this.userPrincipal.getUsername();
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    super.setAuthenticated(authenticated);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
  }
}
