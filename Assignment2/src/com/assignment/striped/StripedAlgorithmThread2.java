package com.assignment.striped;

import com.assignment.utils.Matrix;

public class StripedAlgorithmThread2 extends Thread {
  private final int rowIndex;
  private final double[] row;
  private final Matrix B;
  private final Matrix result;

  public StripedAlgorithmThread2(double[] row, int rowIndex, Matrix B, Matrix result) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.B = B;
    this.result = result;
  }

  @Override
  public void run() {
    for (int j = 0; j < B.getSizeAxis1(); j++) {
      double temp = 0;
      for (int i = 0; i < row.length; i++) {
        temp += row[i] * B.matrix[i][j];
      }
      result.matrix[rowIndex][j] = temp;
    }
    // System.out.println(Thread.currentThread().getName());
  }
}
