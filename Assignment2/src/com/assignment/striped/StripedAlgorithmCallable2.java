package com.assignment.striped;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class StripedAlgorithmCallable2 implements Callable<Map<String, Number>> {
  private final int rowIndex;
  private final int columnIndex;
  private final double[] row;
  private final double[] column;

  public StripedAlgorithmCallable2(double[] row, int rowIndex, double[] column, int columnIndex) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.column = column;
    this.columnIndex = columnIndex;
  }

  @Override
  public Map<String, Number> call() {
    Map<String, Number> result = new HashMap<>();
    double value = 0;
    for (int i = 0; i < row.length; i++) {
      value += row[i] * column[i];
    }

    result.put("value", value);
    result.put("rowIndex", this.rowIndex);
    result.put("columnIndex", this.columnIndex);
    return result;
    // System.out.println(Thread.currentThread().getName());
  }
}
