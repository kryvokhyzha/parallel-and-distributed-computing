package com.assignment.part3;

public class Counter {
    private int count = 0;

    public void incrementUnsynchronized() {
        count++;
    }

    public void decrementUnsynchronized() {
        count--;
    }

    public synchronized void incrementSynchronizedMethod() {
        count++;
    }

    public synchronized void decrementSynchronizedMethod() {
        count--;
    }

    public void incrementSynchronizedBlock() {
        synchronized (this) {
            count++;
        }
    }

    public void decrementSynchronizedBlock() {
        synchronized (this) {
            count--;
        }
    }

    public int getCount() {
        return count;
    }
}
