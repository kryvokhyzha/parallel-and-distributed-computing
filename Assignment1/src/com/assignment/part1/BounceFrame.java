package com.assignment.part1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private final ObjectCanvas canvas;
    private static final int N_BLUE_BALLS = 1000;
    private static final int N_RED_BALLS = 1;
    private static final int N_JOIN_EXPERIMENT = 10;
    public static final int WIDTH = 850;
    public static final int HEIGHT = 750;

    public BounceFrame() {

        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new ObjectCanvas();

        Pool p1 = new Pool(canvas, Color.CYAN, 0 ,0);
        canvas.add(p1);
        Pool p2 = new Pool(canvas, Color.CYAN, WIDTH ,0);
        canvas.add(p2);
        Pool p3 = new Pool(canvas, Color.CYAN, 0, HEIGHT-100);
        canvas.add(p3);
        Pool p4 = new Pool(canvas, Color.CYAN, WIDTH ,HEIGHT-100);
        canvas.add(p4);

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
        JButton buttonJoinExperiment = new JButton("Join experiment");
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

        buttonJoinExperiment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable updatePanel = new Runnable() {
                        public void run() {
                            createJoinExperiment();
                        }
                    };
                Thread t = new Thread(updatePanel);
                t.start();
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
        buttonPanel.add(buttonJoinExperiment);
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

    private void createBall(Color color, int priority, int x, int y, int TIME) {
        Ball b = new Ball(canvas, color, x, y);
        canvas.add(b);

        BallThread thread = new BallThread(b);
        thread.setTIME(TIME);
        thread.setPriority(priority);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread name = " + thread.getName());
    }

    private void createPriorityExperiment() {
        Color color = Color.BLUE;
        for(int i = 0; i < N_BLUE_BALLS; i++){
            createBall(color, 1, WIDTH / 2, HEIGHT / 2);
        }

        color = Color.RED;
        for(int i = 0; i < N_RED_BALLS; i++){
            createBall(color, 10, WIDTH / 2, HEIGHT / 2);
        }
    }

    private void createJoinExperiment() {
        int TIME = 1000;

        for (int i = 0; i < N_JOIN_EXPERIMENT; i++) {
            Color color;
            if (i % 2 == 0) {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }
            createBall(color, Thread.MIN_PRIORITY, WIDTH / 2, HEIGHT / 2, TIME);

        }
    }
}