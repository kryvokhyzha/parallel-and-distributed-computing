package com.assignment.striped;

import com.assignment.*;
import com.assignment.utils.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class StripedAlgorithm implements Algorithm {
  Matrix A;
  Matrix B;
  private final int nThread;

  public StripedAlgorithm(Matrix A, Matrix B, int nThread) {
    this.A = A;
    this.B = B;
    this.nThread = nThread;
  }

  @Override
  public Matrix multiply() {
    Matrix C = new Matrix(A.getSizeAxis0(), B.getSizeAxis1());

    C = version1(C);
    // C = version2(C);
    // C = version3(C);
    // C = version4(C);

    return C;
  }

  public Matrix version1(Matrix C) {
    B.transpose();

    ExecutorService executor = Executors.newFixedThreadPool(this.nThread);

    List<Future<Map<String, Number>>> list = new ArrayList<>();

    for (int j = 0; j < B.getSizeAxis0(); j++) {
      for (int i = 0; i < A.getSizeAxis0(); i++) {
        Callable<Map<String, Number>> worker =
            new StripedAlgorithmCallable2(A.getRow(i), i, B.getRow(j), j);
        Future<Map<String, Number>> submit = executor.submit(worker);
        list.add(submit);
      }
    }

    for (Future<Map<String, Number>> mapFuture : list) {
      try {
        Map<String, Number> res = mapFuture.get();
        C.matrix[(int) res.get("rowIndex")][(int) res.get("columnIndex")] =
            (double) res.get("value");
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();

    return C;
  }

  public Matrix version2(Matrix C) {
    ExecutorService executor = Executors.newFixedThreadPool(this.nThread);

    List<Future<double[]>> list = new ArrayList<>();

    for (int i = 0; i < A.getSizeAxis0(); i++) {
      Callable<double[]> worker = new StripedAlgorithmCallable(A.getRow(i), i, B);
      Future<double[]> submit = executor.submit(worker);
      list.add(submit);
    }

    for (int i = 0; i < list.size(); i++) {
      try {
        C.matrix[i] = list.get(i).get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();

    return C;
  }

  public Matrix version3(Matrix C) {
    ArrayList<StripedAlgorithmThread> threads = new ArrayList<>();

    for (int i = 0; i < A.getSizeAxis0(); i++) {
      StripedAlgorithmThread t = new StripedAlgorithmThread(A.getRow(i), i, B);
      threads.add(t);
      threads.get(i).start();
    }

    for (StripedAlgorithmThread t : threads) {
      try {
        t.join();
        C.matrix[t.getRowIndex()] = t.getResult();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return C;
  }

  public Matrix version4(Matrix C) {
    ArrayList<StripedAlgorithmThread2> threads = new ArrayList<>();

    for (int i = 0; i < A.getSizeAxis0(); i++) {
      StripedAlgorithmThread2 t = new StripedAlgorithmThread2(A.getRow(i), i, B, C);
      threads.add(t);
      threads.get(i).start();
    }

    for (StripedAlgorithmThread2 t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return C;
  }
}
