package com.assignment.striped;

import com.assignment.utils.Matrix;
import java.util.concurrent.Callable;

public class StripedAlgorithmCallable implements Callable<double[]> {
  private final int rowIndex;
  private final double[] row;
  private Matrix B;
  private double[] result;

  public StripedAlgorithmCallable(double[] row, int rowIndex) {
    this.row = row;
    this.rowIndex = rowIndex;
  }

  public StripedAlgorithmCallable(double[] row, int rowIndex, Matrix B) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.B = B;
    this.result = new double[B.getSizeAxis1()];
  }

  @Override
  public double[] call() {
    for (int j = 0; j < B.getSizeAxis1(); j++) {
      // double[] column = B.getColumn(j);
      for (int i = 0; i < row.length; i++) {
        // result[j] += row[i] * column[i];
        result[j] += row[i] * B.matrix[i][j];
      }
    }

    // System.out.println(Thread.currentThread().getName());
    return this.result;
  }

  public int getRowIndex() {
    return rowIndex;
  }
}
