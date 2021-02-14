package com.assignment.striped;

public class StripedAlgorithmThread extends Thread {
  private double value = 0;
  private final int rowIndex;
  private int columnIndex;
  private final double[] row;
  private double[] column;
  private final double[] result;
  public boolean isStop = false;
  public boolean isWait = false;

  public StripedAlgorithmThread(double[] row, int rowIndex, int sizeAxis1B) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.result = new double[sizeAxis1B];
  }

  public StripedAlgorithmThread(
      double[] row, int rowIndex, double[] column, int columnIndex, int sizeAxis1B) {
    this.row = row;
    this.rowIndex = rowIndex;
    this.column = column;
    this.columnIndex = columnIndex;
    this.result = new double[sizeAxis1B];
  }

  @Override
  public void run() {
    value = 0;
    for (int i = 0; i < row.length; i++) {
      value += row[i] * column[i];
    }
    result[this.columnIndex] = value;
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

  public double[] getResult() {
    return this.result;
  }
}
