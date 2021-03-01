package com.assignment.task2;

import com.assignment.task2.basic.BasicAlgorithm;
import com.assignment.task2.fox.FoxAlgorithm;
import com.assignment.task2.utils.Matrix;

public class Main {
  public static void main(String[] args) {
    simpleRun(false);
  }

  public static void simpleRun(boolean printMatrices) {
    int sizeAxis0 = 1000;
    int sizeAxis1 = 1000;

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    // A.matrix = new double[][] {{1.1, 1, 1}, {2, 2.1, 2}, {3, 3, 3}};
    // B.matrix = new double[][] {{1, 1, 1}, {2, 5, 2}, {3, 3, 3}};

    int nThread = Runtime.getRuntime().availableProcessors(); // sizeAxis0

    BasicAlgorithm ba = new BasicAlgorithm(A, B);
    FoxAlgorithm fa = new FoxAlgorithm(A, B, nThread);

    long currTime = System.nanoTime();
    Matrix C = ba.multiply();
    currTime = System.nanoTime() - currTime;

    if (printMatrices) C.print();

    System.out.println("Time for Basic Algorithm: " + currTime / 1_000_000);

    currTime = System.nanoTime();
    C = fa.multiply();
    currTime = System.nanoTime() - currTime;

    if (printMatrices) C.print();

    System.out.println("Time for Fox Algorithm: " + currTime / 1_000_000);
    System.out.println("\n");
  }
}
