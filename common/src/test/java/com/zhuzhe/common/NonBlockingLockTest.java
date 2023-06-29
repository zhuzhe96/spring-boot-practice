package com.zhuzhe.common;

// 非阻塞锁,当一个线程获得锁,其他线程没有获得锁时,其他线程直接返回失败
// 这里的实现并非真正意义上的非阻塞,因为你给方法加上了synchronized,进去的线程本来就会有阻塞效果,只是在进去方法后第一时间判断锁标志并返回而实现一个非阻塞效果
@SuppressWarnings("all")
public class NonBlockingLockTest {
  private boolean isLocked = false;

  public synchronized boolean lock() throws InterruptedException {
    while (isLocked) {
      System.out.printf("BlockedLockTest对象已锁住, 线程%s执行失败.%n", Thread.currentThread().getName());
      return false;
    }
    System.out.printf("BlockedLockTest对象锁已被线程%s持有.%n", Thread.currentThread().getName());
    isLocked = true;
    wait(1);
    System.out.println("解锁结果: " + unlock());
    return true;
  }

  public synchronized boolean unlock() {
    if (isLocked) {
      System.out.printf("BlockedLockTest对象锁已被线程%s解除", Thread.currentThread().getName());
      isLocked = false;
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    var lockTest = new NonBlockingLockTest();
    // 创建100个线程去获取锁
    for (int i = 0; i < 100; i++) {
      new Thread(
              () -> {
                try {
                  lockTest.lock();
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              })
          .start();
    }
  }
}
