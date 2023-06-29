package com.zhuzhe.common;

import java.util.concurrent.atomic.AtomicReference;

// 自旋锁测试
public class SpinLockTest {
  private AtomicReference<Thread> owner = new AtomicReference<>();
  private int count = 0;

  public void lock(){
    var current = Thread.currentThread();
    if (current==owner.get()){
      count++;
      System.out.printf("%s获取锁, count=%d %n",current.getName(),count);
      return;
    }
    System.out.printf("%s开始自旋获取锁 %n",current.getName());
    while (!owner.compareAndSet(null, current)){

    }
    count++;
    System.out.printf("%s获取锁, count=%d %n",current.getName(),count);
  }
  public void unlock(){
    var current = Thread.currentThread();
    if (current==owner.get()){
      count--;
      System.out.printf("%s释放锁，count=%d %n",current.getName(),count);
      if (count==0){
        owner.compareAndSet(current,null);
      }
    }
  }

  // 测试方法
  public void performTask() throws InterruptedException {
    lock();
    try {
      System.out.printf("%s调用performTask() %n", Thread.currentThread().getName());
      performNextedTask();
    } finally {
      unlock();
    }
  }

  public void performNextedTask() throws InterruptedException {
    lock();
    try {
      System.out.printf("%s调用performNextedTask() %n", Thread.currentThread().getName());
    } finally {
      unlock();
    }
  }

  public static void main(String[] args){
    var lockTest = new SpinLockTest();
    for (int i = 0; i < 10; i++) {
      new Thread(
          () -> {
            try {
              lockTest.performTask();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          })
          .start();
    }
  }
}
