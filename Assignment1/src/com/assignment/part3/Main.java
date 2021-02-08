package com.assignment.part3;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Counter counter1 = new Counter();

        Runnable part51 = () -> {run51(counter1);};
        Thread part51Thread = new Thread(part51);

        part51Thread.start();
        part51Thread.join();

        System.out.println("Counter for part 5.1 = " + counter1.getCount());


        Counter counter2 = new Counter();

        Runnable part52 = () -> {
            try {
                run52(counter2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread part52Thread = new Thread(part52);

        part52Thread.start();
        part52Thread.join();

        System.out.println("Counter for part 5.2 = " + counter2.getCount());


        Counter counter3 = new Counter();

        Runnable part53 = () -> {
            try {
                run53(counter3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread part53Thread = new Thread(part53);

        part53Thread.start();
        part53Thread.join();

        System.out.println("Counter for part 5.3 = " + counter3.getCount());


        Counter counter4 = new Counter();

        Runnable part54 = () -> {
            try {
                run54(counter4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread part54Thread = new Thread(part54);

        part54Thread.start();
        part54Thread.join();

        System.out.println("Counter for part 5.4 = " + counter4.getCount());
    }

    public static void run51(Counter counter) {
        int n_times = 1_000_000;

        Runnable task1 = () -> {for(int i = 0; i < n_times; i++) counter.incrementUnsynchronized();};
        Thread taskThread1 =  new Thread(task1);

        Runnable task2 = () -> {for(int i = 0; i < n_times; i++) counter.decrementUnsynchronized();};
        Thread taskThread2 =  new Thread(task2);

        taskThread1.start();
        taskThread2.start();
    }

    public static void run52(Counter counter) throws InterruptedException {
        int n_times = 1_000_000;

        Runnable task1 = () -> {for(int i = 0; i < n_times; i++) counter.incrementSynchronizedMethod();};
        Thread taskThread1 =  new Thread(task1);

        Runnable task2 = () -> {for(int i = 0; i < n_times; i++) counter.decrementSynchronizedMethod();};
        Thread taskThread2 =  new Thread(task2);

        taskThread1.start();
        taskThread2.start();

        taskThread1.join();
        taskThread2.join();
    }

    public static void run53(Counter counter) throws InterruptedException {
        int n_times = 1_000_000;

        Runnable task1 = () -> {for(int i = 0; i < n_times; i++) counter.incrementSynchronizedBlock();};
        Thread taskThread1 =  new Thread(task1);

        Runnable task2 = () -> {for(int i = 0; i < n_times; i++) counter.decrementSynchronizedBlock();};
        Thread taskThread2 =  new Thread(task2);

        taskThread1.start();
        taskThread2.start();

        taskThread1.join();
        taskThread2.join();
    }

    public static void run54(Counter counter) throws InterruptedException {
        int n_times = 1_000_000;

        ReentrantLock lock = new ReentrantLock();

        Runnable task1 = () -> {for(int i = 0; i < n_times; i++) {lock.lock(); counter.incrementUnsynchronized(); lock.unlock();}};
        Thread taskThread1 =  new Thread(task1);

        Runnable task2 = () -> {for(int i = 0; i < n_times; i++) {lock.lock(); counter.decrementUnsynchronized(); lock.unlock();}};
        Thread taskThread2 =  new Thread(task2);

        taskThread1.start();
        taskThread2.start();

        taskThread1.join();
        taskThread2.join();
    }
}
