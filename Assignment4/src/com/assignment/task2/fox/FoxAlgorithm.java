package com.assignment.task2.fox;

import com.assignment.task2.utils.Matrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class FoxAlgorithm extends RecursiveTask<Matrix> {
  Matrix A;
  Matrix B;
  Matrix C;
  private int nBlocks;
  private final int step;
  private final int[][] matrixOfSizesI;
  private final int[][] matrixOfSizesJ;

  public FoxAlgorithm(Matrix A, Matrix B, int nBlocks) {
    this.A = A;
    this.B = B;
    this.C = new Matrix(A.getSizeAxis0(), B.getSizeAxis1());
    this.nBlocks = nBlocks;

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

    this.nBlocks = Math.min(this.nBlocks, A.getSizeAxis0());
    this.nBlocks = findNearestDivider(this.nBlocks, A.getSizeAxis0());
    this.step = A.getSizeAxis0() / this.nBlocks;

    this.matrixOfSizesI = new int[this.nBlocks][this.nBlocks];
    this.matrixOfSizesJ = new int[this.nBlocks][this.nBlocks];

    preparing();
  }

  private int findNearestDivider(int s, int p) {
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

  public void preparing() {
    int stepI = 0;
    for (int i = 0; i < nBlocks; i++) {
      int stepJ = 0;
      for (int j = 0; j < nBlocks; j++) {
        matrixOfSizesI[i][j] = stepI;
        matrixOfSizesJ[i][j] = stepJ;
        stepJ += this.step;
      }
      stepI += this.step;
    }
  }

  private Matrix copyBlock(Matrix matrix, int i, int j, int size) {
    Matrix block = new Matrix(size, size);
    for (int k = 0; k < size; k++) {
      System.arraycopy(matrix.matrix[k + i], j, block.matrix[k], 0, size);
    }
    return block;
  }

  @Override
  protected Matrix compute() {
    List<RecursiveTask<HashMap<String, Object>>> tasks = new ArrayList<>();

    for (int l = 0; l < nBlocks; l++) {
      for (int i = 0; i < nBlocks; i++) {
        for (int j = 0; j < nBlocks; j++) {
          int stepI0 = matrixOfSizesI[i][j];
          int stepJ0 = matrixOfSizesJ[i][j];

          int stepI1 = matrixOfSizesI[i][(i + l) % nBlocks];
          int stepJ1 = matrixOfSizesJ[i][(i + l) % nBlocks];

          int stepI2 = matrixOfSizesI[(i + l) % nBlocks][j];
          int stepJ2 = matrixOfSizesJ[(i + l) % nBlocks][j];

          FoxAlgorithmForkJoin task =
              new FoxAlgorithmForkJoin(
                  copyBlock(A, stepI1, stepJ1, step),
                  copyBlock(B, stepI2, stepJ2, step),
                  stepI0,
                  stepJ0);

          tasks.add(task);
          task.fork();
        }
      }
    }

    for (RecursiveTask<HashMap<String, Object>> task : tasks) {
      HashMap<String, Object> r = task.join();

      Matrix blockRes = (Matrix) r.get("blockRes");
      int stepI = (int) r.get("stepI");
      int stepJ = (int) r.get("stepJ");

      for (int i = 0; i < blockRes.getSizeAxis0(); i++) {
        for (int j = 0; j < blockRes.getSizeAxis1(); j++) {
          C.matrix[i + stepI][j + stepJ] += blockRes.matrix[i][j];
        }
      }
    }

    return this.C;
  }
}
