#include <stdio.h>
#include <stdlib.h>

#include "mpi.h"
#define NRA 62
/* number of rows in matrix A */
#define NCA 15
/* number of columns in matrix A */
#define NCB 7
/* number of columns in matrix B */
#define MASTER 0
/* taskid of first task */
#define FROM_MASTER 1 /* setting a message type */
#define FROM_WORKER 2 /* setting a message type */

int main(int argc, char *argv[]) {
  int numtasks, taskid, numworkers, source, dest, rows,
      /* rows of matrix A sent to each worker */
      averow, extra, offset, i, j, k;

  double a[NRA][NCA], /* matrix A to be multiplied */
      b[NCA][NCB],    /* matrix B to be multiplied */
      c[NRA][NCB];    /* result matrix C */

  MPI_Init(&argc, &argv);

  double t1 = MPI_Wtime();

  MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
  MPI_Comm_rank(MPI_COMM_WORLD, &taskid);

  if (numtasks < 2) {
    printf("Need at least two MPI tasks. Quitting...\n");
    MPI_Abort(MPI_COMM_WORLD, -1);
    exit(1);
  }

  numworkers = numtasks - 1;

  if (taskid == MASTER) {
    printf("mpi_mm has started with %d tasks.\n", numtasks);

    for (i = 0; i < NRA; i++)
      for (j = 0; j < NCA; j++) a[i][j] = 10;
    for (i = 0; i < NCA; i++)
      for (j = 0; j < NCB; j++) b[i][j] = 10;

    averow = NRA / numworkers;
    extra = NRA % numworkers;
    offset = 0;

    MPI_Status send_status[numworkers * 4], recv_status[numworkers * 3];
    MPI_Request send_req[numworkers * 4], recv_req[numworkers * 3];

    for (dest = 1; dest <= numworkers; dest++) {
      rows = (dest <= extra) ? averow + 1 : averow;
      printf("Sending %d rows to task %d offset=%d\n", rows, dest, offset);
      MPI_Isend(&offset, 1, MPI_INT, dest, FROM_MASTER, MPI_COMM_WORLD,
                &send_req[(dest - 1) * 4]);
      MPI_Isend(&rows, 1, MPI_INT, dest, FROM_MASTER, MPI_COMM_WORLD,
                &send_req[(dest - 1) * 4 + 1]);
      MPI_Isend(&a[offset][0], rows * NCA, MPI_DOUBLE, dest, FROM_MASTER,
                MPI_COMM_WORLD, &send_req[(dest - 1) * 4 + 2]);
      MPI_Isend(&b, NCA * NCB, MPI_DOUBLE, dest, FROM_MASTER, MPI_COMM_WORLD,
                &send_req[(dest - 1) * 4 + 3]);

      offset = offset + rows;
    }

    MPI_Waitall(numworkers * 4, &send_req[0], &send_status[0]);

    /* Receive results from worker tasks */
    for (source = 1; source <= numworkers; source++) {
      MPI_Irecv(&offset, 1, MPI_INT, source, FROM_WORKER, MPI_COMM_WORLD,
                &recv_req[(source - 1) * 3]);
      MPI_Irecv(&rows, 1, MPI_INT, source, FROM_WORKER, MPI_COMM_WORLD,
                &recv_req[(source - 1) * 3 + 1]);

      MPI_Wait(&recv_req[(source - 1) * 3], &recv_status[(source - 1) * 3]);
      MPI_Wait(&recv_req[(source - 1) * 3 + 1],
               &recv_status[(source - 1) * 3 + 1]);

      MPI_Irecv(&c[offset][0], rows * NCB, MPI_DOUBLE, source, FROM_WORKER,
                MPI_COMM_WORLD, &recv_req[(source - 1) * 3 + 2]);

      MPI_Wait(&recv_req[(source - 1) * 3 + 2],
               &recv_status[(source - 1) * 3 + 2]);
      printf("Received results from task %d\n", source);
    }

    /* Print results */
    printf("****\n");
    printf("Result Matrix:\n");

    for (i = 0; i < NRA; i++) {
      printf("\n");
      for (j = 0; j < NCB; j++) printf("%6.2f ", c[i][j]);
    }

    printf("\n********\n");
    printf("Done.\n");
  }
  /******** worker task *****************/
  else { /* if (taskid > MASTER) */
    MPI_Status send_status[3], recv_status[4];
    MPI_Request send_req[3], recv_req[4];

    MPI_Irecv(&offset, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD,
              &recv_req[0]);
    MPI_Irecv(&rows, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD,
              &recv_req[1]);
    MPI_Wait(&recv_req[0], &recv_status[0]);
    MPI_Wait(&recv_req[1], &recv_status[1]);

    MPI_Irecv(&a, rows * NCA, MPI_DOUBLE, MASTER, FROM_MASTER, MPI_COMM_WORLD,
              &recv_req[2]);
    MPI_Irecv(&b, NCA * NCB, MPI_DOUBLE, MASTER, FROM_MASTER, MPI_COMM_WORLD,
              &recv_req[3]);

    MPI_Wait(&recv_req[2], &recv_status[2]);
    MPI_Wait(&recv_req[3], &recv_status[3]);

    for (k = 0; k < NCB; k++)
      for (i = 0; i < rows; i++) {
        c[i][k] = 0.0;
        for (j = 0; j < NCA; j++) c[i][k] = c[i][k] + a[i][j] * b[j][k];
      }

    MPI_Isend(&offset, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD,
              &send_req[0]);
    MPI_Isend(&rows, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD,
              &send_req[1]);
    MPI_Wait(&send_req[0], &send_status[0]);
    MPI_Wait(&send_req[1], &send_status[1]);

    MPI_Isend(&c, rows * NCB, MPI_DOUBLE, MASTER, FROM_WORKER, MPI_COMM_WORLD,
              &send_req[2]);

    MPI_Wait(&send_req[2], &send_status[2]);
  }

  t1 = MPI_Wtime() - t1;

  double totalTime;
  MPI_Reduce(&t1, &totalTime, 1, MPI_DOUBLE, MPI_MAX, MASTER, MPI_COMM_WORLD);

  if (taskid == MASTER) {
    printf("\nExecution time: %.2f\n", t1);
  }

  MPI_Finalize();
}