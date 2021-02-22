package com.assignment.task1;

public class TransferThread extends Thread {
  private final Bank bank;
  private final int fromAccount;
  private final int maxAmount;
  private static final int REPS = 1000;

  public TransferThread(Bank b, int from, int max) {
    bank = b;
    fromAccount = from;
    maxAmount = max;
  }

  public void run() {
    try {
      while (!interrupted()) {
        for (int i = 0; i < REPS; i++) {
          int toAccount = (int) (bank.size() * Math.random());
          int amount = (int) (maxAmount * Math.random() / REPS);
          bank.transfer(fromAccount, toAccount, amount);
          Thread.sleep(1);
        }
      }
    } catch (InterruptedException ignored) {
    }
  }
}
