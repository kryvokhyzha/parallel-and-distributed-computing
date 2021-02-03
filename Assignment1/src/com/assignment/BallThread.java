package com.assignment;

public class BallThread extends Thread {
    private final Ball ball;

    public BallThread(Ball ball){
        this.ball = ball;
    }
    @Override
    public void run(){
        try {
            for(int i=1; (i < 10000) & (!Thread.currentThread().isInterrupted()); i++){
                ball.move();

                if(ball.isInPool) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);

            }
        } catch(InterruptedException ignored) {
            System.out.println("Thread " + Thread.currentThread().getName() + " was interrupted!");
        }
    }
}
