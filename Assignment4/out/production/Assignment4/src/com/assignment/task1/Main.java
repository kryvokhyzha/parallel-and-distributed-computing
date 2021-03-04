package com.assignment.task1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) throws IOException {
    List<String> words = TextLoader.getWordsFromFile("resources/text.txt");

    System.out.printf("Number of words: %d\n\n", words.size());

    long currTime = System.nanoTime();
    ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    HashMap<String, Integer> res = pool.submit(new ForkJoinWordCount(words)).join();
    long currTimeForkJoin = System.nanoTime() - currTime;

    System.out.printf("Execution time (ForkJoin): %d\n", currTimeForkJoin);

    System.out.printf("Number of uniq words: %d\n", res.keySet().size());
    System.out.printf(
        "Mean length: %f\n",
        res.values().stream().mapToDouble(i -> i).sum()
            / res.values().stream().mapToDouble(i -> i).count());
    System.out.printf(
        "Var length: %f\n",
        (res.values().stream().mapToDouble(i -> Math.pow(i, 2)).sum()
                / res.values().stream().mapToDouble(i -> i).count())
            - Math.pow(
                (double) res.values().stream().mapToDouble(i -> i).sum()
                    / res.values().stream().mapToDouble(i -> i).count(),
                2));
    System.out.println();

    currTime = System.nanoTime();
    res = SimpleWordCount.processing(words);
    long currTimeSimple = System.nanoTime() - currTime;

    System.out.printf("Execution time (Single Thread): %d\n", currTimeSimple);

    System.out.printf("SpeadUp = %.2f\n", (double) currTimeSimple / currTimeForkJoin);
    /*for (String w : res.keySet()) {
      System.out.println(w + " " + res.get(w));
    }*/
  }
}
