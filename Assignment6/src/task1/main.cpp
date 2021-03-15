#include <cstdio>
#include <cstdlib>

#include "mpi.h"
#define NRA 1000
/* number of rows in matrix A */
#define NCA 1000
/* number of columns in matrix A */
#define NCB 1000
/* number of columns in matrix B */
#define MASTER 0
/* taskid of first task */
#define FROM_MASTER 1  /* setting a message type */
#define FROM_WORKER 10 /* setting a message type */

double **alloc_2d(int rows, int cols) {
  double *mem = (double *)malloc(rows * cols * sizeof(double));
  double **A = (double **)malloc(rows * sizeof(double *));

  A[0] = mem;
  for (int i = 1; i < rows; i++) A[i] = A[i - 1] + cols;

  return A;
}

void free_memory(double ***arr) {
  free(arr[0]);
  free(arr);
}

int main(int argc, char *argv[]) {
  int numtasks, taskid, numworkers, source, dest, rows,
      /* rows of matrix A sent to each worker */
      averow, extra, offset, i, j, k;

  double **a = alloc_2d(NRA, NCA); /* matrix A to be multiplied */
  double **b = alloc_2d(NCA, NCB); /* matrix B to be multiplied */
  double **c = alloc_2d(NRA, NCB); /* result matrix C */

  MPI_Init(&argc, &argv);

  MPI_Status status;

  MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
  MPI_Comm_rank(MPI_COMM_WORLD, &taskid);

  if (numtasks < 2) {
    printf("Need at least two MPI tasks. Quitting...\n");
    MPI_Abort(MPI_COMM_WORLD, -1);
    exit(1);
  }

  numworkers = numtasks - 1;

  if (taskid == MASTER) {
    printf("mpi_mm has started with %d tasks (task1).\n", numtasks);

    for (i = 0; i < NRA; i++)
      for (j = 0; j < NCA; j++) a[i][j] = 10;
    for (i = 0; i < NCA; i++)
      for (j = 0; j < NCB; j++) b[i][j] = 10;

    double t1 = MPI_Wtime();

    averow = NRA / numworkers;
    extra = NRA % numworkers;
    offset = 0;

    for (dest = 1; dest <= numworkers; dest++) {
      rows = (dest <= extra) ? averow + 1 : averow;
      printf("Sending %d rows to task %d offset=%d\n", rows, dest, offset);

      MPI_Send(&offset, 1, MPI_INT, dest, FROM_MASTER, MPI_COMM_WORLD);
      MPI_Send(&rows, 1, MPI_INT, dest, FROM_MASTER + 1, MPI_COMM_WORLD);
      MPI_Send(a[offset], rows * NCA, MPI_DOUBLE, dest, FROM_MASTER + 2,
               MPI_COMM_WORLD);
      MPI_Send(b[0], NCA * NCB, MPI_DOUBLE, dest, FROM_MASTER + 3,
               MPI_COMM_WORLD);

      offset = offset + rows;
    }

    /* Receive results from worker tasks */
    for (source = 1; source <= numworkers; source++) {
      MPI_Recv(&offset, 1, MPI_INT, source, FROM_WORKER, MPI_COMM_WORLD,
               &status);
      MPI_Recv(&rows, 1, MPI_INT, source, FROM_WORKER + 1, MPI_COMM_WORLD,
               &status);
      MPI_Recv(c[offset], rows * NCB, MPI_DOUBLE, source, FROM_WORKER + 2,
               MPI_COMM_WORLD, &status);

      printf("Received results from task %d\n", source);
    }

    /* Print results */
    /*
    printf("****\n");
    printf("Result Matrix:\n");

    for (i = 0; i < NRA; i++)
     {
      printf("\n");
      for (j = 0; j < NCB; j++) printf("%6.2f ", c[i][j]);
    }

    printf("\n********\n");
    printf("Done.\n");*/

    t1 = MPI_Wtime() - t1;

    printf("\nExecution time: %.2f\n", t1);
  }
  /******** worker task *****************/
  else { /* if (taskid > MASTER) */
    MPI_Recv(&offset, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD, &status);
    MPI_Recv(&rows, 1, MPI_INT, MASTER, FROM_MASTER + 1, MPI_COMM_WORLD,
             &status);
    MPI_Recv(a[0], rows * NCA, MPI_DOUBLE, MASTER, FROM_MASTER + 2,
             MPI_COMM_WORLD, &status);
    MPI_Recv(b[0], NCA * NCB, MPI_DOUBLE, MASTER, FROM_MASTER + 3,
             MPI_COMM_WORLD, &status);

    for (k = 0; k < NCB; k++)
      for (i = 0; i < rows; i++) {
        c[i][k] = 0.0;
        for (j = 0; j < NCA; j++) c[i][k] = c[i][k] + a[i][j] * b[j][k];
      }

    MPI_Send(&offset, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD);
    MPI_Send(&rows, 1, MPI_INT, MASTER, FROM_WORKER + 1, MPI_COMM_WORLD);
    MPI_Send(c[0], rows * NCB, MPI_DOUBLE, MASTER, FROM_WORKER + 2,
             MPI_COMM_WORLD);
  }

  free_memory(&a);
  free_memory(&b);
  free_memory(&c);

  MPI_Finalize();
}