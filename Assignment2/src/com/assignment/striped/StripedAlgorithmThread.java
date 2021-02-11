package com.assignment.striped;

public class StripedAlgorithmThread extends Thread {
  private double value = 0;
  private final int rowIndex;
  private int columnIndex;
  private final double[] row;
  private double[] column;

  public StripedAlgorithmThread(double[] row, int rowIndex) {
    this.row = row;
    this.rowIndex = rowIndex;
  }

  @Override
  public void run() {
    value = 0;
    for (int i = 0; i < row.length; i++) {
      value += row[i] * column[i];
    }
    // System.out.println(Thread.currentThread().getName());
  }

  public double getValue() {
    return value;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public void setColumn(double[] column) {
    this.column = column;
  }

  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }
}
