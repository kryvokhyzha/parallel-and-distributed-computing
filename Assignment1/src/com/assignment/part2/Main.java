package com.assignment.part2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable task1 = () -> {System.out.println("Part 5.1:"); run51();};
        Thread taskThread1 =  new Thread(task1);
        taskThread1.start();
        taskThread1.join();

        Thread.sleep(10);

        Runnable task2 = () -> {System.out.println("\nPart 5.2:"); run52();};
        Thread taskThread2 =  new Thread(task2);
        taskThread2.start();
        taskThread2.join();
    }

    public static void run51() {
        PrintThread1 p1 = new PrintThread1("-");
        PrintThread1 p2 = new PrintThread1("|");

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);

        t1.start();
        t2.start();
    }

    public static void run52() {
        Control control = new Control();
        PrintThread2 p1 = new PrintThread2("-", control);
        PrintThread2 p2 = new PrintThread2("|", control);

        p1.start();
        p2.start();
    }
}
