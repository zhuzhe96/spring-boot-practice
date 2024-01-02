package com.zhuzhe.jdknewfeature.algorithm;

import java.util.Arrays;

/**
 * 稀疏数组
 */
public class A001_SparseArray {

  public static void main(String[] args) {
    var arr = new int[7][8];
    arr[3][7] = 1;
    arr[5][2] = 2;
    arr[4][4] = 3;
    arr[6][1] = 4;
    for (int[] row : arr) {
      System.out.println(Arrays.toString(row));
    }
    System.out.println("====================zip=====================");
    var zipArr = zip(arr);
    for (int[] row : zipArr) {
      System.out.println(Arrays.toString(row));
    }
    System.out.println("====================unzip=====================");
    var unzipArr = unzip(zipArr);
    for (int[] row : unzipArr) {
      System.out.println(Arrays.toString(row));
    }
  }

  private static int[][] zip(int[][] source) {
    var validCount = 0;
    for (int[] row : source) {
      for (int e : row) {
        if (e != 0) {
          validCount++;
        }
      }
    }
    var zipArr = new int[validCount + 1][3];
    zipArr[0][0] = source.length;
    zipArr[0][1] = source[0].length;
    zipArr[0][2] = validCount;

    var pointer = 1;
    for (int i = 0; i < zipArr[0][0]; i++) {
      for (int j = 0; j < zipArr[0][1]; j++) {
        if (source[i][j] != 0) {
          zipArr[pointer][0] = i;
          zipArr[pointer][1] = j;
          zipArr[pointer][2] = source[i][j];
          pointer++;
        }
      }
    }
    return zipArr;
  }

  private static int[][] unzip(int[][] zipArr) {
    var unzipArr = new int[zipArr[0][0]][zipArr[0][1]];
    for (int i = 1; i < zipArr.length; i++) {
      unzipArr[zipArr[i][0]][zipArr[i][1]] = zipArr[i][2];
    }
    return unzipArr;
  }
}
