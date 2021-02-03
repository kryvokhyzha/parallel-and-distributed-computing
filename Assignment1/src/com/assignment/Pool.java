package com.assignment;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pool {
    private final Component canvas;
    private static final int RADIUS = 40;
    private static final int DIAMETER = RADIUS * 2;
    private int x = 0;
    private int y = 0;
    private final Color color;

    public Pool(Component c, Color color) {
        this.canvas = c;
        this.color = color;
    }

    public Pool(Component c, Color color, int x, int y) {
        this.canvas = c;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public static void f(){
        int a = 0;
    }

    public void draw (Graphics2D g2){
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getRADIUS() {
        return RADIUS;
    }
}
