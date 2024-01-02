package com.zhuzhe.jdknewfeature.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 环形队列
 * size=10,tail=12, tail = (tail+1)%size = 3
 *
 * <p>ArrayList扩容机制:</p>
 * <p>1. 添加元素方法: {@link ArrayList#add(Object)}->{@link ArrayList#add(Object, Object[], int)} </p>
 * <p>2. 判断当前容器已满, 进行扩容{@link ArrayList#grow()}</p>
 *
 */
public class A003_RingQueue {
  private static class RingQueue {
    private int head;// 头元素的位置
    private int tail;// 尾元素的后一位
    private int size;
    private int[] data;

    public RingQueue(int size) {
      this.size = size;
      data = new int[size];
    }

    public boolean isFull() {
      return (tail+1)%size==head;
    }

    public boolean isEmpty() {
      return tail == head;
    }

    public void add(int val) {
      if (isFull()) {
        System.out.println("队列已满");
        return;
      }
      data[tail] = val;
      tail = (tail+1)%size;// tail++的环形实现
    }

    public int getHead() {
      if (isEmpty()) {
        throw new RuntimeException("队列为空");
      }
      int value = data[head];
      head = (head+1) % size;// head++的环形实现
      return value;
    }

    public void show(){
      System.out.println("head = " + head);
      System.out.println("tail = " + tail);
      System.out.println(Arrays.toString(data));
    }

    public int size() {
      return (tail + size - head) % size;
    }

    public void showValid() {
      if (isEmpty()) {
        System.out.println("[]");
        return;
      }
      for (int i = head; i < head + size(); i++) {
        System.out.printf("arr[%d]=%d\n", i%size, data[i%size]);
      }
    }
  }
  public static void main(String[] args) {
    var ringQueue = new RingQueue(8);
    ringQueue.add(1);
    ringQueue.add(2);
    ringQueue.add(3);
    ringQueue.add(4);
    ringQueue.add(5);
    ringQueue.getHead();
    ringQueue.getHead();
    ringQueue.getHead();
    ringQueue.getHead();
    ringQueue.add(6);
    ringQueue.add(7);
    ringQueue.add(8);
    ringQueue.add(9);
    ringQueue.add(10);
    ringQueue.add(11);
    ringQueue.add(12);
    ringQueue.show();
    ArrayList<String> list = new ArrayList<String>();
    list.add("0001");
    list.add("0002");
    list.add("0003");
  }
}
