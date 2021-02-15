package com.assignment;

import com.assignment.basic.BasicAlgorithm;
import com.assignment.striped.StripedAlgorithm;
import com.assignment.utils.Matrix;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    // simpleRun(false);
    threadNExperiment();
    sizeMatrixExperiment();
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

    int nThread = sizeAxis0;

    BasicAlgorithm ba = new BasicAlgorithm(A, B);
    StripedAlgorithm sa = new StripedAlgorithm(A, B, nThread);

    long currTime = System.nanoTime();
    Matrix C = ba.multiply();
    currTime = System.nanoTime() - currTime;

    if (printMatrices) C.print();

    System.out.println("Time for Basic Algorithm: " + currTime / 1_000_000);

    currTime = System.nanoTime();
    C = sa.multiply();
    currTime = System.nanoTime() - currTime;

    if (printMatrices) C.print();

    System.out.println("Time for Striped Algorithm: " + currTime / 1_000_000);
    System.out.println("\n");
  }

  public static void threadNExperiment() {
    int sizeAxis0 = 1000;
    int sizeAxis1 = 1000;
    int nExperiments = 5;

    int[] threadsN = new int[] {5, 10, 20, 50, 100, 500, 1000};
    Map<Integer, Long> timeResult = new HashMap<>();

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    for (int nThread : threadsN) {
      StripedAlgorithm sa = new StripedAlgorithm(A, B, nThread);

      long acc = 0;
      for (int i = 0; i < nExperiments; i++) {
        long currTime = System.nanoTime();
        Matrix C = sa.multiply();
        acc += System.nanoTime() - currTime;
      }
      acc /= nExperiments;

      timeResult.put(nThread, acc / 1_000_000);
    }

    List<Integer> keys = timeResult.keySet().stream().sorted().collect(Collectors.toList());

    System.out.println("**Number of threads experiment**");

    System.out.printf("%30s", "Number of thread:");
    for (int key : keys) {
      System.out.printf("%10d", key);
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time for Striped:");
    for (int key : keys) {
      System.out.printf("%10d", timeResult.get(key));
      System.out.print(" ");
    }
    System.out.println("\n");
  }

  public static void sizeMatrixExperiment() {
    int nThread = 100;
    int nExperiments = 5;

    int[] sizesArray = new int[] {100, 500, 1000, 1500, 2000};
    Map<Integer, Long> timeResult = new HashMap<>();

    for (int size : sizesArray) {
      Matrix A = new Matrix(size, size);
      Matrix B = new Matrix(size, size);

      A.generateRandomMatrix();
      B.generateRandomMatrix();

      StripedAlgorithm sa = new StripedAlgorithm(A, B, nThread);

      long acc = 0;
      for (int i = 0; i < nExperiments; i++) {
        long currTime = System.nanoTime();
        Matrix C = sa.multiply();
        acc += System.nanoTime() - currTime;
      }
      acc /= nExperiments;

      timeResult.put(size, acc / 1_000_000);
    }

    List<Integer> keys = timeResult.keySet().stream().sorted().collect(Collectors.toList());

    System.out.println("**Size of matrix experiment**");

    System.out.printf("%30s", "Size of matrix:");
    for (int key : keys) {
      System.out.printf("%10d", key);
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time for Striped:");
    for (int key : keys) {
      System.out.printf("%10d", timeResult.get(key));
      System.out.print(" ");
    }
    System.out.println("\n");
  }
}
