package com.assignment.basic;

import com.assignment.Algorithm;
import com.assignment.Matrix;

public class BasicAlgorithm implements Algorithm {
  Matrix A;
  Matrix B;

  public BasicAlgorithm(Matrix A, Matrix B) {
    this.A = A;
    this.B = B;
  }

  @Override
  public Matrix multiply() {
    Matrix C = new Matrix(A.getSizeAxis0(), B.getSizeAxis1());

    for (int i = 0; i < A.getSizeAxis0(); i++) {
      for (int j = 0; j < B.getSizeAxis1(); j++) {
        C.matrix[i][j] = 0;
        for (int k = 0; k < A.getSizeAxis1(); k++) {
          C.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
        }
      }
    }
    return C;
  }
}
