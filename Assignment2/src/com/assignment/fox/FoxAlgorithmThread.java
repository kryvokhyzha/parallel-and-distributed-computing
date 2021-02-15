package com.assignment.fox;

import com.assignment.utils.Matrix;

public class FoxAlgorithmThread extends Thread {
  private final Matrix A;
  private final Matrix B;
  private final Matrix C;

  private final int stepI;
  private final int stepJ;

  public FoxAlgorithmThread(Matrix A, Matrix B, Matrix C, int stepI, int stepJ) {
    this.A = A;
    this.B = B;
    this.C = C;

    this.stepI = stepI;
    this.stepJ = stepJ;
  }

  @Override
  public void run() {
    Matrix blockRes = multiplyBlock();

    for (int i = 0; i < blockRes.getSizeAxis0(); i++) {
      for (int j = 0; j < blockRes.getSizeAxis1(); j++) {
        C.matrix[i + stepI][j + stepJ] += blockRes.matrix[i][j];
      }
    }
    // System.out.println(Thread.currentThread().getName());
  }

  private Matrix multiplyBlock() {
    Matrix blockRes = new Matrix(A.getSizeAxis1(), B.getSizeAxis0());
    for (int i = 0; i < A.getSizeAxis0(); i++) {
      for (int j = 0; j < B.getSizeAxis1(); j++) {
        for (int k = 0; k < A.getSizeAxis1(); k++) {
          blockRes.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
        }
      }
    }
    return blockRes;
  }
}
