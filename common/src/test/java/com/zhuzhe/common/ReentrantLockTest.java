package com.zhuzhe.common;

import java.util.concurrent.locks.ReentrantLock;

// 可重入锁测试，使用了两种方式实现. 同一线程外层函数获得锁之后，内存递归函数仍然有获得该锁
public class ReentrantLockTest {
  public static void main(String[] args) {
    // 基于synchronized实现的可重入锁
//    var lockTest1 = new LockTest1();
//    for (int i = 0; i < 10; i++) {
//      new Thread(
//              () -> {
//                try {
//                  lockTest1.performTask();
//                } catch (InterruptedException e) {
//                  throw new RuntimeException(e);
//                }
//              })
//          .start();
//    }

    // 基于ReentrantLock实现的可重入锁
    var lockTest2 = new LockTest2();
    for (int i = 0; i < 10; i++) {
      new Thread(
          () -> {
            try {
              lockTest2.performTask();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          })
          .start();
    }
  }
}

/*基于synchronized实现的可重入锁*/
class LockTest1 {
  // 锁标志
  private boolean isLocked = false;
  // 持有锁的线程
  private Thread lockedBy = null;
  // 线程锁数量
  private int lockCount = 0;

  public synchronized void lock() throws InterruptedException {
    Thread callingThread = Thread.currentThread();
    // 如果锁已被其他线程持有，则当前线程等待
    while (isLocked && lockedBy != callingThread) {
      System.out.printf("%s阻塞%n", Thread.currentThread().getName());
      wait();
    }
    // 给当前线程上锁
    isLocked = true;
    lockedBy = callingThread;
    lockCount++;
    System.out.printf("%s获得锁, lockCount=%d %n", Thread.currentThread().getName(), lockCount);
  }

  public synchronized void unlock() {
    if (Thread.currentThread() == lockedBy) {
      lockCount--;
      System.out.printf("%s解除锁, lockCount=%d %n", Thread.currentThread().getName(), lockCount);
      if (lockCount == 0) {
        System.out.printf("%s已解除全部锁%n", Thread.currentThread().getName());
        isLocked = false;
        notify();
      }
    }
  }

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
      System.out.printf("%s 调用performNextedTask() %n", Thread.currentThread().getName());
    } finally {
      unlock();
    }
  }
}

class LockTest2 {
  ReentrantLock lock = new ReentrantLock();

  public void performTask() throws InterruptedException {
    lock.lock();
    System.out.printf("%s获得锁, lockCount=%d %n", Thread.currentThread().getName(), lock.getHoldCount());
    try {
      System.out.printf("%s调用performTask() %n", Thread.currentThread().getName());
      performNextedTask();
    } finally {
      lock.unlock();
      System.out.printf("%s解除锁, lockCount=%d %n", Thread.currentThread().getName(), lock.getHoldCount());
    }
  }

  public void performNextedTask() throws InterruptedException {
    lock.lock();
    System.out.printf("%s获得锁, lockCount=%d %n", Thread.currentThread().getName(), lock.getHoldCount());
    try {
      System.out.printf("%s调用performNextedTask() %n", Thread.currentThread().getName());
    } finally {
      lock.unlock();
      System.out.printf("%s解除锁, lockCount=%d %n", Thread.currentThread().getName(), lock.getHoldCount());
    }
  }
}
