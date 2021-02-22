package com.assignment.task2;

public class Drop {
  // Message sent from producer
  // to consumer.
  private int message;
  // True if consumer should wait
  // for producer to send message,
  // false if producer should wait for
  // consumer to retrieve message.
  private boolean empty = true;

  public synchronized int take() {
    // Wait until message is
    // available.
    while (empty) {
      try {
        wait();
      } catch (InterruptedException ignored) {
      }
    }
    // Toggle status.
    empty = true;
    // Notify producer that
    // status has changed.
    notifyAll();
    return message;
  }

  public synchronized void put(int message) {
    // Wait until message has
    // been retrieved.
    while (!empty) {
      try {
        wait();
      } catch (InterruptedException ignored) {
      }
    }
    // Toggle status.
    empty = false;
    // Store message.
    this.message = message;
    // Notify consumer that status
    // has changed.
    notifyAll();
  }
}
