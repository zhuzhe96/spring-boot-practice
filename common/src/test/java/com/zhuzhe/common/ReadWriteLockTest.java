package com.zhuzhe.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
  private volatile Map<String, Object> map = new HashMap<>();
  private ReadWriteLock rwLock = new ReentrantReadWriteLock();

  public void set(String key, Object value){
    // 加写锁
    rwLock.writeLock().lock();
    System.out.printf("%s进行写操作 key=%s %n",Thread.currentThread().getName(), key);
    try {
      TimeUnit.MICROSECONDS.sleep(300);
      map.put(key,value);
      System.out.printf("%s写完数据 key=%s %n",Thread.currentThread().getName(), key);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }finally{
      //释放写锁
      rwLock.writeLock().unlock();
    }
  }

  public Object get(String key){
    // 加读锁
    rwLock.readLock().lock();
    Object result = null;
    try {
      System.out.printf("%s进行读操作 key=%s %n",Thread.currentThread().getName(), key);
      TimeUnit.MICROSECONDS.sleep(300);
      result = map.get(key);
      System.out.printf("%s读完数据 key=%s %n",Thread.currentThread().getName(), key);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }finally{
      // 释放读锁
      rwLock.readLock().unlock();
    }
    return result;
  }

  public static void main(String[] args) throws InterruptedException {
    var lockTest = new ReadWriteLockTest();
    for(int i = 0; i < 5; i++) {
      final int num = i;
      new Thread(()->{
        lockTest.set(num+"",num+"");
      },String.valueOf(num)).start();
    }
    TimeUnit.MICROSECONDS.sleep(300);
    for(int i = 0; i < 5; i++) {
      final int num = i;
      new Thread(()->{
        lockTest.get(num+"");
      },String.valueOf(num)).start();
    }
  }
}
