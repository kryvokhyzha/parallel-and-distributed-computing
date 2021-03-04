package com.assignment.task1;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinWordCount extends RecursiveTask<HashMap<String, Integer>> {
  private final List<String> words;
  private final HashMap<String, Integer> wordCounts = new HashMap<>();
  private static final int THRESHOLD = 20000;

  public ForkJoinWordCount(List<String> words) {
    this.words = words;
  }

  @Override
  protected HashMap<String, Integer> compute() {
    if (this.words.size() > THRESHOLD) {
      ForkJoinTask.invokeAll(createSubtask()).stream()
          .map(ForkJoinTask::join)
          .flatMap(map -> map.entrySet().stream())
          .forEach(entry -> this.wordCounts.putIfAbsent(entry.getKey(), entry.getValue()));
      return this.wordCounts;
    } else {
      return processing(words);
    }
  }

  private Collection<ForkJoinWordCount> createSubtask() {
    List<ForkJoinWordCount> dividedTasks = new ArrayList<>();
    dividedTasks.add(new ForkJoinWordCount(words.subList(0, words.size() / 2)));
    dividedTasks.add(new ForkJoinWordCount(words.subList(words.size() / 2, words.size())));
    return dividedTasks;
  }

  private HashMap<String, Integer> processing(List<String> words) {
    for (String word : words) {
      this.wordCounts.putIfAbsent(word, word.length());
    }
    return this.wordCounts;
  }
}
