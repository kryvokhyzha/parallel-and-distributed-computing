package com.assignment.part1;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
  private final Component canvas;
  private static final int RADIUS = 10;
  private static final int DIAMETER = RADIUS * 2;
  private int x = 0;
  private int y = 0;
  private int dx = 2;
  private int dy = 2;
  private final Color color;
  public boolean isInPool = false;

  public Ball(Component c, Color color) {
    this.canvas = c;
    this.color = color;

    if (Math.random() < 0.5) {
      x = new Random().nextInt(this.canvas.getWidth());
      y = 0;
    } else {
      x = 0;
      y = new Random().nextInt(this.canvas.getHeight());
    }
  }

  public Ball(Component c, Color color, int x, int y) {
    this.canvas = c;
    this.color = color;

    this.x = x;
    this.y = y;
  }

  public static void f() {
    int a = 0;
  }

  public void draw(Graphics2D g2) {
    g2.setColor(this.color);
    g2.fill(new Ellipse2D.Double(x, y, DIAMETER, DIAMETER));
  }

  public void move() {
    x += dx;
    y += dy;
    if (x < 0) {
      x = 0;
      dx = -dx;
    }
    if (x + DIAMETER >= this.canvas.getWidth()) {
      x = this.canvas.getWidth() - DIAMETER;
      dx = -dx;
    }

    if (y < 0) {
      y = 0;
      dy = -dy;
    }
    if (y + DIAMETER >= this.canvas.getHeight()) {
      y = this.canvas.getHeight() - DIAMETER;
      dy = -dy;
    }

    this.canvas.revalidate();
    this.canvas.repaint();
  }

  public boolean isInPool(Pool pool) {
    double distance =
        Math.sqrt(Math.pow(pool.getX() - this.x, 2) + Math.pow(pool.getY() - this.y, 2));

    return distance + RADIUS < Pool.getRADIUS();
  }
}
