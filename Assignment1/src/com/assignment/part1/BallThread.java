package com.assignment.part1;

import javax.swing.*;

public class BallThread extends Thread {
  private final Ball ball;
  private int TIME = 10000;

  public BallThread(Ball ball) {
    this.ball = ball;
  }

  @Override
  public void run() {
    try {
      for (int i = 1; (i < TIME) & (!Thread.currentThread().isInterrupted()); i++) {

        ball.move();

        if (ball.isInPool) {
          // Thread.currentThread().interrupt();
          break;
        }

        System.out.println("Thread name = " + Thread.currentThread().getName());
        Thread.sleep(5);
      }
    } catch (InterruptedException ignored) {
      System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted!");
    }
  }

  public void setTIME(int TIME) {
    this.TIME = TIME;
  }
}
