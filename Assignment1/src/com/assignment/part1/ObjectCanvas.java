package com.assignment.part1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ObjectCanvas extends JPanel {
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final ArrayList<Pool> pools = new ArrayList<>();

    public void add(Ball b){
        this.balls.add(b);
    }

    public void add(Pool p){
        this.pools.add(p);
    }

    public void remove(Ball b) {
        Score.increase();
        this.balls.remove(b);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        try {
            for (Ball b : balls) {
                for (Pool p : pools) {
                    if (b.isInPool(p)) {
                        b.isInPool = true;
                        remove(b);
                        break;
                    }
                }
                if (!b.isInPool) {
                    b.draw(g2);
                }
            }
        } catch (Exception ignored) {
        }

        for (Pool p : pools) {
            p.draw(g2);
        }
    }
}
