package com.zhuzhe.securityrbac.security.service;

import com.zhuzhe.securityrbac.common.Consts;
import com.zhuzhe.securityrbac.config.JwtConfig;
import com.zhuzhe.securityrbac.entity.vo.UserPrincipal;
import com.zhuzhe.securityrbac.utils.JwtUtil;
import com.zhuzhe.securityrbac.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtConfig.class)
public class TokenService {
  // token->jwt->claims->redis->user

  @Autowired
  private RedisUtil redisUtil;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private JwtConfig jwtConfig;
  public long SIXTY_MINUTE = 60L * 60 * 60;

  /*从请求中解析用户信息 request->jwt->claims->user*/
  public UserPrincipal getUserPrincipal(HttpServletRequest request) {
    String jwt = jwtUtil.getJwtFromRequest(request);
    if (StringUtils.isNotBlank(jwt)) {
      Claims claims = parseJwt(jwt);
      var userId = claims.get(Consts.JWT_CLAIM_KEY, Long.class);
      String redisKey = getUserRedisToken(userId);
      return redisUtil.getCacheObject(redisKey);
    }
    return null;
  }

  /*根据用户id获取Redis信息*/
  public UserPrincipal getUserPrincipal(Long userId) {
    String redisKey = getUserRedisToken(userId);
    return redisUtil.getCacheObject(redisKey);
  }


  /*设置用户信息到Redis*/
  public void setUserPrincipal(UserPrincipal userPrincipal) {
    if (userPrincipal != null && userPrincipal.getId() != null) {
      String redisKey = getUserRedisToken(userPrincipal.getId());
      redisUtil.setCacheObject(redisKey, userPrincipal, jwtConfig.getTtl(), TimeUnit.MILLISECONDS);
    }
  }

  /*从Redis中删除用户信息*/
  @SuppressWarnings("unused")
  public void invalidateToken(Long userId) {
    String redisKey = getUserRedisToken(userId);
    redisUtil.deleteObject(redisKey);
  }

  /*检查用户是否接近过期, 是则刷新过期时间*/
  public void verifyTokenExpire(UserPrincipal userPrincipal) {
    long expireTime = userPrincipal.getExpireTime() == null ? 0 : userPrincipal.getExpireTime();
    long currentTime = System.currentTimeMillis();
    if (expireTime - currentTime < SIXTY_MINUTE) {
      refreshRedisToken(userPrincipal);
    }
  }

  /*根据用户信息生成Token*/
  public String createJwt(UserPrincipal userPrincipal) {
    Map<String, Object> claims = new HashMap<>();
    refreshRedisToken(userPrincipal);
    String uuid = UUID.randomUUID().toString();
    claims.put("uuid", uuid);
    claims.put(Consts.JWT_CLAIM_KEY, userPrincipal.getId());
    return jwtUtil.createJWT(claims);
  }

  /*更新Redis中的用户*/
  public void refreshRedisToken(UserPrincipal userPrincipal) {
    userPrincipal.setLoginTime(new Date());
    var redisKey = getUserRedisToken(userPrincipal.getId());
    userPrincipal.setExpireTime(userPrincipal.getLoginTime().getTime() + jwtConfig.getTtl());
    redisUtil.setCacheObject(redisKey, userPrincipal, jwtConfig.getTtl(), TimeUnit.MILLISECONDS);
  }

  /*解析jwt*/
  public Claims parseJwt(String jwt) {
    return jwtUtil.parseJWTToClaims(jwt);
  }

  /*Redis Key*/
  public String getUserRedisToken(Long userId) {
    return Consts.REDIS_USER_PREFIX + userId;
  }
}
