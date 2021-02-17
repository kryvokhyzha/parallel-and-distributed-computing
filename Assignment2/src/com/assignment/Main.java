package com.assignment;

import com.assignment.basic.BasicAlgorithm;
import com.assignment.fox.FoxAlgorithm;
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

    int nThread = Runtime.getRuntime().availableProcessors(); // sizeAxis0

    BasicAlgorithm ba = new BasicAlgorithm(A, B);
    StripedAlgorithm sa = new StripedAlgorithm(A, B, nThread);
    FoxAlgorithm fa = new FoxAlgorithm(A, B, nThread);

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

    currTime = System.nanoTime();
    C = fa.multiply();
    currTime = System.nanoTime() - currTime;

    if (printMatrices) C.print();

    System.out.println("Time for Fox Algorithm: " + currTime / 1_000_000);
    System.out.println("\n");
  }

  public static void threadNExperiment() {
    int sizeAxis0 = 1000;
    int sizeAxis1 = 1000;
    int nExperiments = 3;

    int[] threadsNStriped = new int[] {5, 10, 50, 100, 500, 1000};
    int[] threadsNFox = new int[] {4, 5, 10, 25, 50, 75};
    Map<Integer, Long> timeResultStriped = new HashMap<>();
    Map<Integer, Long> timeResultFox = new HashMap<>();

    Matrix A = new Matrix(sizeAxis0, sizeAxis1);
    Matrix B = new Matrix(sizeAxis0, sizeAxis1);

    A.generateRandomMatrix();
    B.generateRandomMatrix();

    for (int nThread : threadsNStriped) {
      StripedAlgorithm sa = new StripedAlgorithm(A, B, nThread);

      long acc = 0;
      for (int i = 0; i < nExperiments; i++) {
        long currTime = System.nanoTime();
        Matrix C = sa.multiply();
        acc += System.nanoTime() - currTime;
      }
      acc /= nExperiments;

      timeResultStriped.put(nThread, acc / 1_000_000);
    }

    for (int nThread : threadsNFox) {
      FoxAlgorithm fa = new FoxAlgorithm(A, B, nThread);

      long acc = 0;
      for (int i = 0; i < nExperiments; i++) {
        long currTime = System.nanoTime();
        Matrix C = fa.multiply();
        acc += System.nanoTime() - currTime;
      }
      acc /= nExperiments;

      timeResultFox.put(nThread, acc / 1_000_000);
    }

    List<Integer> keysStriped =
        timeResultStriped.keySet().stream().sorted().collect(Collectors.toList());

    System.out.println("**Number of threads experiment (Striped)**");

    System.out.printf("%30s", "Number of thread:");
    for (int key : keysStriped) {
      System.out.printf("%10d", key);
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time:");
    for (int key : keysStriped) {
      System.out.printf("%10d", timeResultStriped.get(key));
      System.out.print(" ");
    }
    System.out.println("\n");

    List<Integer> keysFox = timeResultFox.keySet().stream().sorted().collect(Collectors.toList());

    System.out.println("**Number of threads experiment (Fox)**");

    System.out.printf("%30s", "Number of thread:");
    for (int key : keysFox) {
      System.out.printf("%10d", key);
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time:");
    for (int key : keysFox) {
      System.out.printf("%10d", timeResultFox.get(key));
      System.out.print(" ");
    }
    System.out.println("\n");
  }

  public static void sizeMatrixExperiment() {
    int nThread = Runtime.getRuntime().availableProcessors();
    int nExperiments = 3;

    int[] sizesArray = new int[] {10, 100, 500, 1000, 1500};
    Map<Integer, Long> timeResultStriped = new HashMap<>();
    Map<Integer, Long> timeResultFox = new HashMap<>();

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

      timeResultStriped.put(size, acc / 1_000_000);

      FoxAlgorithm fa = new FoxAlgorithm(A, B, nThread);

      acc = 0;
      for (int i = 0; i < nExperiments; i++) {
        long currTime = System.nanoTime();
        Matrix C = fa.multiply();
        acc += System.nanoTime() - currTime;
      }
      acc /= nExperiments;

      timeResultFox.put(size, acc / 1_000_000);
    }

    List<Integer> keys = timeResultStriped.keySet().stream().sorted().collect(Collectors.toList());

    System.out.println("**Size of matrix experiment**");

    System.out.printf("%30s", "Size of matrix:");
    for (int key : keys) {
      System.out.printf("%10d", key);
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time for Striped:");
    for (int key : keys) {
      System.out.printf("%10d", timeResultStriped.get(key));
      System.out.print(" ");
    }

    System.out.println();

    System.out.printf("%30s", "Time for Fox:");
    for (int key : keys) {
      System.out.printf("%10d", timeResultFox.get(key));
      System.out.print(" ");
    }

    System.out.println("\n");
  }
}
