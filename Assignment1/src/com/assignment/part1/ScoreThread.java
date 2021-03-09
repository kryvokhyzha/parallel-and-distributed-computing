package com.assignment.part1;

import javax.swing.*;

public class ScoreThread extends Thread {
  private final JLabel label;

  public ScoreThread(JLabel label) {
    this.label = label;
  }

  @Override
  public void run() {
    System.out.println("Daemon thread name = " + Thread.currentThread().getName());
    while (!Thread.currentThread().isInterrupted()) {
      label.setText("Score: " + Score.getScore());
      label.repaint();
      try {
        Thread.sleep(5);
      } catch (InterruptedException ignored) {
      }
    }
  }
}
