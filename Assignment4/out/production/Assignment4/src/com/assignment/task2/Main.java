package com.assignment.task2;

import com.assignment.task2.basic.BasicAlgorithm;
import com.assignment.task2.fox.FoxAlgorithm;
import com.assignment.task2.utils.Matrix;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) {
    simpleRun(false);
  }

  public static void simpleRun(boolean printMatrices) {
    int sizeAxis0 = 2000;
    int sizeAxis1 = 2000;

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    // A.matrix = new double[][] {{1.1, 1, 1}, {2, 2.1, 2}, {3, 3, 3}};
    // B.matrix = new double[][] {{1, 1, 1}, {2, 5, 2}, {3, 3, 3}};

    int nThread = Runtime.getRuntime().availableProcessors();

    BasicAlgorithm ba = new BasicAlgorithm(A, B);

    long currTimeBasic = System.nanoTime();
    Matrix C = ba.multiply();
    currTimeBasic = System.nanoTime() - currTimeBasic;

    if (printMatrices) C.print();

    System.out.println("Time for Basic Algorithm: " + currTimeBasic / 1_000_000);

    long currTimeFox = System.nanoTime();
    ForkJoinPool forkJoinPool = new ForkJoinPool(nThread);
    C = forkJoinPool.invoke(new FoxAlgorithm(A, B, 6));
    currTimeFox = System.nanoTime() - currTimeFox;

    if (printMatrices) C.print();

    System.out.println("Time for Fox Algorithm: " + currTimeFox / 1_000_000);
    System.out.println(
        "SpeedUp (timeBasic / timeFoxForkJoin): " + (double) currTimeBasic / currTimeFox);

    if (sizeAxis0 == 2000) {
      System.out.println(
          "SpeedUp (timeFox / timeFoxForkJoin): " + (double) 20474 / (currTimeFox / 1_000_000));
    }

    System.out.println("\n");

    //
  }
}
