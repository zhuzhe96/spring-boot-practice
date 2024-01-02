package com.zhuzhe.jdknewfeature.algorithm;

import java.util.Arrays;

/**
 * 数组队列
 */
public class A002_ArrayQueue {

  private static class ArrayQueue {

    private int head;//头元素的前一位
    private int tail;//尾元素的位置
    private int size;
    private int[] data;

    public ArrayQueue(int size) {
      this.size = size;
      data = new int[size];
      head = -1;
      tail = -1;
    }

    public boolean isFull() {
      return tail == size - 1;
    }

    public boolean isEmpty() {
      return head == tail;
    }

    public void addTail(int val) {
      if (isFull()) {
        System.out.println("is full!");
        return;
      }
      tail++;
      data[tail] = val;
    }

    public int getHead() {
      if (isEmpty()) {
        throw new RuntimeException("is empty");
      }
      head++;
      return data[head];
    }

    public void show() {
      System.out.println("head = " + head);
      System.out.println("tail = " + tail);
      System.out.println(Arrays.toString(data));
    }

    public void showValid() {
      System.out.println(Arrays.toString(Arrays.copyOfRange(data, head + 1, tail + 1)));
    }

    public int head() {
      if (isEmpty()) {
        throw new RuntimeException("is empty!");
      }
      return data[head + 1];
    }
  }

  public static void main(String[] args) {
    var arrayQueue = new ArrayQueue(10);
    arrayQueue.addTail(1);
    arrayQueue.addTail(3);
    arrayQueue.addTail(2);
    arrayQueue.addTail(6);
    arrayQueue.addTail(8);
    arrayQueue.addTail(0);
    arrayQueue.show();
    arrayQueue.addTail(8);
    arrayQueue.getHead();
    arrayQueue.getHead();
    arrayQueue.show();
    arrayQueue.showValid();
  }
}
