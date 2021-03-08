package com.assignment;

public class Process extends Element {

  private int queue, maxqueue, failure;
  private double meanQueue;

  public Process(double delay) {
    super(delay);
    queue = 0;
    maxqueue = Integer.MAX_VALUE;
    meanQueue = 0.0;
  }

  @Override
  public void inAct() {
    if (super.getState() == 0) {
      super.setState(1);
      super.setTnext(super.getTcurr() + super.getDelay());
    } else {
      if (getQueue() < getMaxqueue()) {
        setQueue(getQueue() + 1);
      } else {
        failure++;
      }
    }
  }

  @Override
  public void outAct() {
    super.outAct();
    super.setTnext(Double.MAX_VALUE);
    super.setState(0);

    if (getQueue() > 0) {
      setQueue(getQueue() - 1);
      super.setState(1);
      super.setTnext(super.getTcurr() + super.getDelay());
    }
  }

  public int getFailure() {
    return failure;
  }

  public int getQueue() {
    return queue;
  }

  public void setQueue(int queue) {
    this.queue = queue;
  }

  public int getMaxqueue() {
    return maxqueue;
  }

  public void setMaxqueue(int maxqueue) {
    this.maxqueue = maxqueue;
  }

  @Override
  public void printInfo() {
    super.printInfo();
    System.out.println("failure = " + this.getFailure());
  }

  @Override
  public void doStatistics(double delta) {
    meanQueue = getMeanQueue() + queue * delta;
  }

  public double getMeanQueue() {
    return meanQueue;
  }
}
