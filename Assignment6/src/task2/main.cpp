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

int main(int argc, char *argv[]) {
  int numtasks, taskid, numworkers, source, dest, rows,
      /* rows of matrix A sent to each worker */
      averow, extra, offset, i, j, k;

  double a[NRA][NCA]; /* matrix A to be multiplied */
  double b[NCA][NCB]; /* matrix B to be multiplied */
  double c[NRA][NCB]; /* result matrix C */

  MPI_Init(&argc, &argv);

  MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
  MPI_Comm_rank(MPI_COMM_WORLD, &taskid);

  if (numtasks < 2) {
    printf("Need at least two MPI tasks. Quitting...\n");
    MPI_Abort(MPI_COMM_WORLD, -1);
    exit(1);
  }

  numworkers = numtasks - 1;

  if (taskid == MASTER) {
    printf("mpi_mm has started with %d tasks (task 2).\n", numtasks);

    for (i = 0; i < NRA; i++)
      for (j = 0; j < NCA; j++) a[i][j] = 10;
    for (i = 0; i < NCA; i++)
      for (j = 0; j < NCB; j++) b[i][j] = 10;

    double t1 = MPI_Wtime();

    averow = NRA / numworkers;
    extra = NRA % numworkers;
    offset = 0;

    MPI_Status recv_status1[numworkers * 2], recv_status2[numworkers];
    MPI_Request send_req[numworkers * 4], recv_req1[numworkers * 2],
        recv_req2[numworkers];

    for (dest = 1; dest <= numworkers; dest++) {
      rows = (dest <= extra) ? averow + 1 : averow;

      printf("Sending %d rows to task %d offset=%d\n", rows, dest, offset);
      MPI_Isend(&offset, 1, MPI_INT, dest, FROM_MASTER, MPI_COMM_WORLD,
                &send_req[(dest - 1) * 4]);
      MPI_Isend(&rows, 1, MPI_INT, dest, FROM_MASTER + 1, MPI_COMM_WORLD,
                &send_req[(dest - 1) * 4 + 1]);
      MPI_Isend(&a[offset][0], rows * NCA, MPI_DOUBLE, dest, FROM_MASTER + 2,
                MPI_COMM_WORLD, &send_req[(dest - 1) * 4 + 2]);
      MPI_Isend(&b, NCA * NCB, MPI_DOUBLE, dest, FROM_MASTER + 3,
                MPI_COMM_WORLD, &send_req[(dest - 1) * 4 + 3]);

      offset = offset + rows;
    }

    // MPI_Waitall(numworkers * 4, &send_req[0], &send_status[0]);

    /* Receive results from worker tasks */
    int offsets_arr[numworkers];
    int rows_arr[numworkers];

    for (source = 1; source <= numworkers; source++) {
      MPI_Irecv(&offsets_arr[source - 1], 1, MPI_INT, source, FROM_WORKER,
                MPI_COMM_WORLD, &recv_req1[(source - 1) * 2]);
      MPI_Irecv(&rows_arr[source - 1], 1, MPI_INT, source, FROM_WORKER + 1,
                MPI_COMM_WORLD, &recv_req1[(source - 1) * 2 + 1]);
    }

    MPI_Waitall(numworkers * 2, &recv_req1[0], &recv_status1[0]);

    for (source = 1; source <= numworkers; source++) {
      MPI_Irecv(&c[offsets_arr[source - 1]][0], rows_arr[source - 1] * NCB,
                MPI_DOUBLE, source, FROM_WORKER + 2, MPI_COMM_WORLD,
                &recv_req2[source - 1]);
      printf("Received results from task %d\n", source);
    }

    MPI_Waitall(numworkers, &recv_req2[0], &recv_status2[0]);

    /* Print results */
    /*printf("****\n");
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
    MPI_Status recv_status[4];
    MPI_Request send_req[3], recv_req[4];

    MPI_Irecv(&offset, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD,
              &recv_req[0]);
    MPI_Irecv(&rows, 1, MPI_INT, MASTER, FROM_MASTER + 1, MPI_COMM_WORLD,
              &recv_req[1]);
    MPI_Irecv(&b, NCA * NCB, MPI_DOUBLE, MASTER, FROM_MASTER + 3,
              MPI_COMM_WORLD, &recv_req[3]);

    MPI_Wait(&recv_req[0], &recv_status[0]);
    MPI_Wait(&recv_req[1], &recv_status[1]);

    MPI_Isend(&offset, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD,
              &send_req[0]);
    MPI_Isend(&rows, 1, MPI_INT, MASTER, FROM_WORKER + 1, MPI_COMM_WORLD,
              &send_req[1]);

    MPI_Irecv(&a, rows * NCA, MPI_DOUBLE, MASTER, FROM_MASTER + 2,
              MPI_COMM_WORLD, &recv_req[2]);

    MPI_Wait(&recv_req[2], &recv_status[2]);
    MPI_Wait(&recv_req[3], &recv_status[3]);

    for (k = 0; k < NCB; k++)
      for (i = 0; i < rows; i++) {
        c[i][k] = 0.0;
        for (j = 0; j < NCA; j++) c[i][k] = c[i][k] + a[i][j] * b[j][k];
      }

    MPI_Isend(&c, rows * NCB, MPI_DOUBLE, MASTER, FROM_WORKER + 2,
              MPI_COMM_WORLD, &send_req[2]);
  }

  MPI_Finalize();
}