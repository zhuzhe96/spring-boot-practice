package com.zhuzhe.securityrbac.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhuzhe.securityrbac.config.JwtConfig;
import com.zhuzhe.securityrbac.common.Consts;
import com.zhuzhe.securityrbac.common.Status;
import com.zhuzhe.securityrbac.exception.SecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("unused")
@EnableConfigurationProperties(JwtConfig.class)
public class JwtUtil {

  @Autowired
  private JwtConfig jwtConfig;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;
  @Autowired
  private SecretKey signatureKey;

  // 生成jwt
  public String createJWT(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims).signWith(signatureKey).compact();
  }

  // 从请求中获得JWT
  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(Consts.AUTHORIZATION_HEAD);
    if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 解析jwt
  public Claims parseJWTToClaims(String jwt) {
    try {
      return Jwts.parserBuilder().setSigningKey(signatureKey).build().parseClaimsJws(jwt).getBody();
    } catch (ExpiredJwtException e) {
      log.error("Token已过期");
      throw new SecurityException(Status.SC_UNAUTHORIZED);
    } catch (UnsupportedJwtException e) {
      log.error("不支持的Token");
      throw new SecurityException(Status.TOKEN_PARSE_ERROR);
    } catch (MalformedJwtException e) {
      log.error("Token无效");
      throw new SecurityException(Status.TOKEN_PARSE_ERROR);
    } catch (SignatureException e) {
      log.error("无效的Token签名");
      throw new SecurityException(Status.TOKEN_PARSE_ERROR);
    } catch (IllegalArgumentException e) {
      log.error("Token参数不存在");
      throw new SecurityException(Status.TOKEN_PARSE_ERROR);
    }
  }

  // todo 创建JWT并存储到Redis
  public String createJWT(Long id, String subject, String sessionKey,
      Collection<? extends GrantedAuthority> authorities) {
    var now = new Date();
    var builder = Jwts.builder()
        .setId(id.toString())
        .setSubject(subject)
        .setIssuedAt(now)
        .claim(Consts.JWT_SESSION_KEY, sessionKey)
        .claim(Consts.JWT_AUTHORITIES, authorities)
        .signWith(signatureKey);

    // 设置过期时间
    var ttl = jwtConfig.getTtl();
    if (ttl > 0) {
      builder.setExpiration(getExpirationDate(now, ttl));
    }

    var jwt = builder.compact();
    // 将生成的JWT保存到Redis中
    stringRedisTemplate.opsForValue()
        .set(Consts.REDIS_JWT_PREFIX + id, jwt, ttl, TimeUnit.MILLISECONDS);
    return jwt;
  }

  // todo 计算到期时间
  private Date getExpirationDate(Date now, long ttl) {
    var calendar = Calendar.getInstance();
    calendar.setTime(now);
    calendar.add(Calendar.MILLISECOND, (int) ttl);
    return calendar.getTime();
  }

  // todo 生成JWT签名
  public String generateSignature() {
    var secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    return Encoders.BASE64.encode(secretKey.getEncoded());
  }

  // todo 解析JWT
  public Claims parseJWT(String jwt) {
    try {
      var claims = Jwts.parserBuilder().setSigningKey(signatureKey).build().parseClaimsJws(jwt)
          .getBody();
      var id = claims.getId();
      var redisKey = Consts.REDIS_JWT_PREFIX + id;

      // 获取判断key是否过期
      var expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
      if (Objects.isNull(expire) || expire <= 0) {
        throw new SecurityException(Status.TOKEN_EXPIRED);
      }

      // 检查redis中的JWT是否当前一致
      String token = stringRedisTemplate.opsForValue().get(redisKey);
      if (!StringUtils.equals(jwt, token)) {
        throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);
      }
      return claims;
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
             SignatureException | IllegalArgumentException | SecurityException e) {
      log.error("Token 无效");
      throw new RuntimeException(e);
    }
  }

  // todo 设置JWT过期
  public void invalidateJWT(HttpServletRequest request) {
    var jwt = getJwtFromRequest(request);
    var id = getIdFromJWT(jwt);
    stringRedisTemplate.delete(Consts.REDIS_JWT_PREFIX + id);
  }

  // todo 从jwt中获取用户id
  public String getIdFromJWT(String jwt) {
    var claims = parseJWT(jwt);
    return claims.getId();
  }

  // todo 从jwt中获取用户名
  public String getUsernameFromJWT(String jwt) {
    var claims = parseJWT(jwt);
    return claims.getSubject();
  }
}
