package com.assignment.task2.fox;

import com.assignment.task2.utils.Matrix;
import java.util.HashMap;
import java.util.concurrent.RecursiveTask;

public class FoxAlgorithmForkJoin extends RecursiveTask<HashMap<String, Object>> {
  private final Matrix A;
  private final Matrix B;

  private final int stepI;
  private final int stepJ;

  public FoxAlgorithmForkJoin(Matrix A, Matrix B, int stepI, int stepJ) {
    this.A = A;
    this.B = B;

    this.stepI = stepI;
    this.stepJ = stepJ;
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

  @Override
  protected HashMap<String, Object> compute() {
    Matrix blockRes = multiplyBlock();

    HashMap<String, Object> result = new HashMap<>();
    result.put("blockRes", blockRes);
    result.put("stepI", stepI);
    result.put("stepJ", stepJ);

    return result;
  }
}
