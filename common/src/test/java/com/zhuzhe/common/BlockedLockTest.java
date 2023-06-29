package com.zhuzhe.common;

/**
 * 阻塞锁, 多个线程调同一个方法时排队竞争
 */
public class BlockedLockTest {
  private boolean isLocked = false;//对象锁

  public synchronized void lock() throws InterruptedException {
    while (isLocked){
      System.out.printf("BlockedLockTest对象已锁住, 线程%s将等待.%n", Thread.currentThread().getName());
      wait();
    }
    System.out.printf("BlockedLockTest对象锁已被线程%s持有.%n", Thread.currentThread().getName());
    isLocked = true;
    wait(3000);
    unlock();
  }

  public synchronized void unlock(){
    isLocked = false;
    System.out.printf("BlockedLockTest对象锁已被线程%s解除", Thread.currentThread().getName());
    System.out.println("随机唤醒等待的线程");
    notify();
  }

  public static void main(String[] args){
    var lockTest = new BlockedLockTest();
    // 创建100个线程去获取锁
    for(int i = 0; i < 100; i++) {
      new Thread(()->{
        try {
          lockTest.lock();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }).start();
    }
  }
}
