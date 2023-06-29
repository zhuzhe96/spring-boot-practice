package com.zhuzhe.common;

import java.util.concurrent.locks.ReentrantLock;

// 非公平锁
public class UnfairLockTest {
  public static void main(String[] args) throws InterruptedException {
    // 默认构造函数创建的是非公平锁
    ReentrantLock reentrantLock = new ReentrantLock();
    for (int i = 0; i < 10; i++) {
      final int threadNum = i;
      new Thread(() -> {
        reentrantLock.lock();
        System.out.println("线程" + threadNum + "获取锁");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }finally{
          reentrantLock.unlock();
          System.out.println("线程" + threadNum +"释放锁");
        }
      },String.valueOf(threadNum)).start();
      Thread.sleep(999);
    }
  }
}
