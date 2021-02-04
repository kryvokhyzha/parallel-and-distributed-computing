package com.assignment.part1;

import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolManagerImpl;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class BallThread extends Thread {
    private final Ball ball;
    private int TIME = 10000;

    public BallThread(Ball ball){
        this.ball = ball;
    }
    @Override
    public void run(){
        try {
            for(int i = 1; (i < TIME) & (!Thread.currentThread().isInterrupted()); i++) {

                ball.move();

                if(ball.isInPool) {
                    //Thread.currentThread().interrupt();
                    break;
                }

                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);

            }
        } catch(InterruptedException ignored) {
            System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted!");
        }
    }

    public void setTIME(int TIME) {
        this.TIME = TIME;
    }
}
