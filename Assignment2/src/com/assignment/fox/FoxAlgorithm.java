package com.assignment.fox;

import com.assignment.Algorithm;
import com.assignment.utils.Matrix;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoxAlgorithm implements Algorithm {
  Matrix A;
  Matrix B;
  private int nThread;

  public FoxAlgorithm(Matrix A, Matrix B, int nThread) {
    this.A = A;
    this.B = B;
    this.nThread = nThread;
  }

  private int delimeter_3(int s, int p) {
    /*
    https://ru.stackoverflow.com/questions/434403/%D0%9F%D0%BE%D0%B8%D1%81%D0%BA-%D0%B1%D0%BB%D0%B8%D0%B6%D0%B0%D0%B9%D1%88%D0%B5%D0%B3%D0%BE-%D0%B4%D0%B5%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8F
     */
    int i = s;
    while (i > 1) {
      if (p % i == 0) break;
      if (i >= s) {
        i++;
      } else {
        i--;
      }
      if (i > Math.sqrt(p)) i = Math.min(s, p / s) - 1;
    }

    return i >= s ? i : i != 0 ? p / i : p;
  }

  @Override
  public Matrix multiply() {
    Matrix C = new Matrix(A.getSizeAxis0(), B.getSizeAxis1());

    if (!(A.getSizeAxis0() == A.getSizeAxis1()
        & B.getSizeAxis0() == B.getSizeAxis1()
        & A.getSizeAxis0() == B.getSizeAxis0())) {
      try {
        throw new Exception("Matrix A and B have different dimensions!");
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    this.nThread = Math.min(this.nThread, A.getSizeAxis0());
    this.nThread = delimeter_3(this.nThread, A.getSizeAxis0());
    int step = A.getSizeAxis0() / this.nThread;

    ExecutorService exec = Executors.newFixedThreadPool(this.nThread);
    ArrayList<Future> threads = new ArrayList<>();

    int[][] matrixOfSizesI = new int[nThread][nThread];
    int[][] matrixOfSizesJ = new int[nThread][nThread];

    int stepI = 0;
    for (int i = 0; i < nThread; i++) {
      int stepJ = 0;
      for (int j = 0; j < nThread; j++) {
        matrixOfSizesI[i][j] = stepI;
        matrixOfSizesJ[i][j] = stepJ;
        stepJ += step;
      }
      stepI += step;
    }

    for (int l = 0; l < nThread; l++) {
      for (int i = 0; i < nThread; i++) {
        for (int j = 0; j < nThread; j++) {
          int stepI0 = matrixOfSizesI[i][j];
          int stepJ0 = matrixOfSizesJ[i][j];

          int stepI1 = matrixOfSizesI[i][(i + l) % nThread];
          int stepJ1 = matrixOfSizesJ[i][(i + l) % nThread];

          int stepI2 = matrixOfSizesI[(i + l) % nThread][j];
          int stepJ2 = matrixOfSizesJ[(i + l) % nThread][j];

          FoxAlgorithmThread t =
              new FoxAlgorithmThread(
                  copyBlock(A, stepI1, stepJ1, step),
                  copyBlock(B, stepI2, stepJ2, step),
                  C,
                  stepI0,
                  stepJ0);
          threads.add(exec.submit(t));
        }
      }
    }

    for (Future mapFuture : threads) {
      try {
        mapFuture.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    exec.shutdown();

    return C;
  }

  private Matrix copyBlock(Matrix matrix, int i, int j, int size) {
    Matrix block = new Matrix(size, size);
    for (int k = 0; k < size; k++) {
      System.arraycopy(matrix.matrix[k + i], j, block.matrix[k], 0, size);
    }
    return block;
  }
}
