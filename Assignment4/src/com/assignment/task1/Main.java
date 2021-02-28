package com.assignment.task1;

import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) {
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    String[] lines =
        new String[] {
          "t as sd sdf sdf aaaaaaaaaaaaaaaaa",
          "t oeioj fdsh fd",
          "sdfaf sdf dsf ",
          "dsfafa gfsgsfdsdfg dg fdg dfgsfdg sdf",
          "afawsfa dfs gfsd gfd ",
          "awfawf",
          "dfgd",
          "sdff"
        };
    ForkJoinWordCount task = new ForkJoinWordCount(lines);
    forkJoinPool.execute(task);
    HashMap<String, Integer> res = task.join();

    for (String w : res.keySet()) {
      System.out.println(w + " " + res.get(w));
    }
  }
}
