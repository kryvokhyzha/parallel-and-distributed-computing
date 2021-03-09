package com.assignment.part1;

import java.util.ArrayList;
import java.util.List;

public class Score {
  private static int score = 0;

  private static final List<ScoreListener> listeners = new ArrayList<ScoreListener>();

  public static void addListener(ScoreListener toAdd) {
    listeners.add(toAdd);
  }

  public static void increase() {
    Score.score += 1;

    // Notify everybody that may be interested.
    for (ScoreListener hl : listeners) hl.actionPerformed();
  }

  public static int getScore() {
    return score;
  }
}
