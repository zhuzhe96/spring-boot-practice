package com.zhuzhe.securityrbac.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

  @Autowired
  private final RedisTemplate redisTemplate;

  public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
    ValueOperations<String, T> operation = redisTemplate.opsForValue();
    operation.set(key, value);
    return operation;
  }

  public <T> void setCacheObject(String key, T value, Long timeout, TimeUnit timeUnit) {
    ValueOperations<String, T> operation = redisTemplate.opsForValue();
    operation.set(key, value, timeout, timeUnit);
  }

  public <T> T getCacheObject(String key) {
    ValueOperations<String, T> operation = redisTemplate.opsForValue();
    log.info("[Redis]Key={}", key);
    return operation.get(key);
  }

  public void deleteObject(String key) {
    redisTemplate.delete(key);
  }

  public void deleteObject(Collection collection) {
    redisTemplate.delete(collection);
  }

  public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
    ListOperations<String, T> listOperation = redisTemplate.opsForList();
    if (!CollectionUtils.isEmpty(dataList)) {
      int size = dataList.size();
      for (T data : dataList) {
        listOperation.leftPush(key, data);
      }
    }
    return listOperation;
  }

  public <T> List<T> getCacheList(String key) {
    List<T> list = new ArrayList<T>();
    ListOperations<String, T> listOperation = redisTemplate.opsForList();
    Long size = listOperation.size(key);
    for (int i = 0; i < size; i++) {
      list.add(listOperation.index(key, i));
    }
    return list;
  }

  public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
    BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
    for (T data : dataSet){
      setOperation.add(data);
    }
    return setOperation;
  }

  public <T> Set<T> getCacheSet(String key){
    Set<T> set = new HashSet<T>();
    BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
    set = setOperation.members();
    return set;
  }

  public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap){
    HashOperations<String, String, T> hashOperation = redisTemplate.opsForHash();
    if (!CollectionUtils.isEmpty(dataMap)){
      for (Map.Entry<String, T> entry : dataMap.entrySet()){
        hashOperation.put(key, entry.getKey(), entry.getValue());
      }
    }
    return hashOperation;
  }

  public <T> Map<String, T> getCacheMap(String key){
    Map<String, T> map = redisTemplate.opsForHash().entries(key);
    return map;
  }

  public Collection<String> keys(String pattern){
    return redisTemplate.keys(pattern);
  }
}
