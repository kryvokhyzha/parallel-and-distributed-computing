package com.assignment;

import com.assignment.basic.BasicAlgorithm;
import com.assignment.striped.StripedAlgorithm;

public class Main {
  public static void main(String[] args) {
    int sizeAxis0 = 300;
    int sizeAxis1 = 300;

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    // A.matrix = new double[][]{{1, 1, 1}, {2, 2, 2}, {3, 3, 3}};
    // B.matrix = new double[][]{{1, 1, 1}, {2, 5, 2}, {3, 3, 3}};

    BasicAlgorithm ba = new BasicAlgorithm(A, B);
    StripedAlgorithm sa = new StripedAlgorithm(A, B);

    long currTime = System.nanoTime();
    Matrix C = sa.multiply();
    currTime = System.nanoTime() - currTime;

    // C.print();

    System.out.println("Time: " + currTime / 1_000_000);
  }
}
