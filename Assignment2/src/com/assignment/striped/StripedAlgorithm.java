package com.assignment.striped;

import com.assignment.*;
import java.util.ArrayList;

public class StripedAlgorithm implements Algorithm {
  Matrix A;
  Matrix B;

  public StripedAlgorithm(Matrix A, Matrix B) {
    this.A = A;
    this.B = B;
  }

  @Override
  public Matrix multiply() {
    Matrix C = new Matrix(A.getSizeAxis0(), B.getSizeAxis1());

    ArrayList<StripedAlgorithmThread> threads = new ArrayList<>();
    /*
    for (int i = 0; i < A.getSizeAxis0(); i++) {
      for (int j = 0; j < B.getSizeAxis1(); j++) {
        StripedAlgorithmThread t = new StripedAlgorithmThread(A.getRow(i), i);
        //int idx = (i + j) % B.getSizeAxis1();
        t.setColumn(B.getColumn(j));
        t.setColumnIndex(j);
        threads.add(t);
      }
    }

    for (StripedAlgorithmThread t: threads) {
      t.start();
    }

    for (StripedAlgorithmThread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        C.matrix[t.getRowIndex()][t.getColumnIndex()] = t.getValue();
      }
    }*/

    for (int i = 0; i < A.getSizeAxis0(); i++) {
      StripedAlgorithmThread t = new StripedAlgorithmThread(A.getRow(i), i);
      threads.add(t);
    }

    for (int j = 0; j < B.getSizeAxis1(); j++) {
      for (int i = 0; i < threads.size(); i++) {
        StripedAlgorithmThread t = threads.get(i);

        int idx = (i + j) % B.getSizeAxis1();
        t.setColumn(B.getColumn(idx));
        t.setColumnIndex(idx);
        // System.out.println(idx);

        // System.out.println(t.getName() + " " + t.getState());
        t.start();
      }

      for (StripedAlgorithmThread t : threads) {
        try {
          t.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          C.matrix[t.getRowIndex()][t.getColumnIndex()] = t.getValue();
        }
      }
    }

    return C;
  }
}
