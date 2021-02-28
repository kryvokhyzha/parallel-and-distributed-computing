package com.assignment.task3;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    Journal journal = new Journal();
    int nWeeks = 3;

    /*Runnable r = () -> {
      (new Thread( new Teacher("Lecturer 1", Arrays.asList("ІС-71", "ІС-72", "ІС-73"), nWeeks, journal))).start();
      (new Thread( new Teacher("Assistant 1", Collections.singletonList("ІС-71"), nWeeks, journal))).start();
      (new Thread( new Teacher("Assistant 2", Collections.singletonList("ІС-72"), nWeeks, journal))).start();
      (new Thread( new Teacher("Assistant 3", Collections.singletonList("ІС-73"), nWeeks, journal))).start();
    };*/

    Runnable r =
        new Runnable() {
          @Override
          public void run() {
            (new Thread(
                    new Teacher(
                        "Lecturer 1", Arrays.asList("ІС-71", "ІС-72", "ІС-73"), nWeeks, journal)))
                .start();
            (new Thread(
                    new Teacher(
                        "Assistant 1", Arrays.asList("ІС-71", "ІС-72", "ІС-73"), nWeeks, journal)))
                .start();
            (new Thread(
                    new Teacher(
                        "Assistant 2", Arrays.asList("ІС-71", "ІС-72", "ІС-73"), nWeeks, journal)))
                .start();
            (new Thread(
                    new Teacher(
                        "Assistant 3", Arrays.asList("ІС-71", "ІС-72", "ІС-73"), nWeeks, journal)))
                .start();
          }
        };
    Thread t = new Thread(r);
    t.start();
    t.join();

    journal.show();
  }
}
