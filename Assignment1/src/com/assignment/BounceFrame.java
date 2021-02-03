package com.assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private final ObjectCanvas canvas;
    private static final int N_BLUE_BALLS = 1000;
    private static final int N_RED_BALLS = 1;
    public static final int WIDTH = 850;
    public static final int HEIGHT = 750;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new ObjectCanvas();

        Pool p = new Pool(canvas, Color.CYAN);
        canvas.add(p);

        System.out.println(new StringBuilder().append("In Frame Thread name = ").append(Thread.currentThread().getName()).toString());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.lightGray);

        JButton buttonAddBlueBall = new JButton("Add blue ball");
        JButton buttonAddRedBall = new JButton("Add red ball");
        JButton buttonPriorityExperiment = new JButton("Red/Blue priority experiment");
        JButton buttonStop = new JButton("Stop");

        JLabel labelScore = new JLabel("Score: " + Score.getScore());
        /*
        // also, we can use daemon thread

        ScoreThread scoreThreadad = new ScoreThread(labelScore);
        scoreThreadad.setDaemon(true);
        scoreThreadad.setPriority(Thread.MAX_PRIORITY);
        scoreThreadad.start();
        */

        Score.addListener(new ScoreListener() {
            @Override
            public void actionPerformed() {
                labelScore.setText("Score: " + Score.getScore());
                labelScore.repaint();
            }
        });

        buttonAddBlueBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = Color.BLUE;
                createBall(color, Thread.NORM_PRIORITY);
            }
        });

        buttonAddRedBall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = Color.RED;
                createBall(color, Thread.NORM_PRIORITY);
            }
        });

        buttonPriorityExperiment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPriorityExperiment();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });


        buttonPanel.add(buttonAddBlueBall);
        buttonPanel.add(buttonAddRedBall);
        buttonPanel.add(buttonPriorityExperiment);
        buttonPanel.add(buttonStop);

        labelPanel.add(labelScore);

        content.add(buttonPanel, BorderLayout.SOUTH);
        content.add(labelPanel, BorderLayout.NORTH);
    }

    private void createBall(Color color, int priority) {
        Ball b = new Ball(canvas, color);
        canvas.add(b);

        BallThread thread = new BallThread(b);
        thread.setPriority(priority);
        thread.start();
        System.out.println("Thread name = " + thread.getName());
    }

    private void createBall(Color color, int priority, int x, int y) {
        Ball b = new Ball(canvas, color, x, y);
        canvas.add(b);

        BallThread thread = new BallThread(b);
        thread.setPriority(priority);
        thread.start();
        System.out.println("Thread name = " + thread.getName());
    }

    private void createPriorityExperiment() {
        Color color = Color.BLUE;
        for(int i = 0; i < N_BLUE_BALLS; i++){
            createBall(color, Thread.MIN_PRIORITY, WIDTH / 2, HEIGHT / 2);
        }

        color = Color.RED;
        for(int i = 0; i < N_RED_BALLS; i++){
            createBall(color, Thread.MAX_PRIORITY, WIDTH / 2, HEIGHT / 2);
        }
    }
}