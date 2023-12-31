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
@SuppressWarnings("unused")
@EnableConfigurationProperties(JwtConfig.class)
public class TokenService {
  @Autowired
  private RedisUtil redisUtil;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private JwtConfig jwtConfig;
  public long SIXTY_MINUTE = 60L * 60 * 60;

  /**
   * 从请求中获取用户信息
   * @param request 请求
   * @return 用户信息
   * request->jwt token->claims->userId->(Redis)user
   */
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

  /**
   * 使用用户id获取用户信息
   * @param userId 用户id
   * @return 用户信息
   * userId->get:(Redis)user
   */
  public UserPrincipal getUserPrincipal(Long userId) {
    String redisKey = getUserRedisToken(userId);
    return redisUtil.getCacheObject(redisKey);
  }


  /**
   * 设置用户信息
   * @param userPrincipal 用户信息
   * user->set:(Redis)user
   */
  public void setUserPrincipal(UserPrincipal userPrincipal) {
    if (userPrincipal != null && userPrincipal.getId() != null) {
      String redisKey = getUserRedisToken(userPrincipal.getId());
      redisUtil.setCacheObject(redisKey, userPrincipal, jwtConfig.getTtl(), TimeUnit.MILLISECONDS);
    }
  }

  /**
   * 删除用户信息
   * @param userId 用户id
   * userId->delete:(Redis)user
   */
  @SuppressWarnings("unused")
  public void invalidateToken(Long userId) {
    String redisKey = getUserRedisToken(userId);
    redisUtil.deleteObject(redisKey);
  }

  /**
   * 验证用户,快过期时就续期
   * @param userPrincipal 用户信息
   * user->verify:(Redis)user
   */
  public void verifyTokenExpire(UserPrincipal userPrincipal) {
    long expireTime = userPrincipal.getExpireTime() == null ? 0 : userPrincipal.getExpireTime();
    long currentTime = System.currentTimeMillis();
    if (expireTime - currentTime < SIXTY_MINUTE) {
      refreshRedisToken(userPrincipal);
    }
  }

  /**
   * 创建签名
   * @param userPrincipal 用户信息
   * @return 签名
   * user->claims->jwt token
   */
  public String createJwt(UserPrincipal userPrincipal) {
    Map<String, Object> claims = new HashMap<>();
    refreshRedisToken(userPrincipal);
    String uuid = UUID.randomUUID().toString();
    claims.put("uuid", uuid);
    claims.put(Consts.JWT_CLAIM_KEY, userPrincipal.getId());
    return jwtUtil.createJWT(claims);
  }

  /**
   * 刷新用户
   * @param userPrincipal 用户信息
   * user->refresh:(Redis)user
   */
  public void refreshRedisToken(UserPrincipal userPrincipal) {
    userPrincipal.setLoginTime(new Date());
    var redisKey = getUserRedisToken(userPrincipal.getId());
    userPrincipal.setExpireTime(userPrincipal.getLoginTime().getTime() + jwtConfig.getTtl());
    redisUtil.setCacheObject(redisKey, userPrincipal, jwtConfig.getTtl(), TimeUnit.MILLISECONDS);
  }

  /**
   * 解析签名
   * @return Claims
   * jwt token->claims
   */
  public Claims parseJwt(String jwt) {
    return jwtUtil.parseJWTToClaims(jwt);
  }

  /**
   * 获取用户的Key
   * @return redisKey
   * userId->(Redis)key
   */
  public String getUserRedisToken(Long userId) {
    return Consts.REDIS_USER_PREFIX + userId;
  }
}
