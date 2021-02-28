package com.assignment.task1;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinWordCount extends RecursiveTask<HashMap<String, Integer>> {
  private final String[] lines;
  private final HashMap<String, Integer> wordCounts = new HashMap<>();
  private static final int THRESHOLD = 1;

  public ForkJoinWordCount(String[] lines) {
    this.lines = lines;
  }

  @Override
  protected HashMap<String, Integer> compute() {
    if (this.lines.length > THRESHOLD) {
      HashMap<String, Integer> tempMap = new HashMap<>();
      ForkJoinTask.invokeAll(createSubtask()).stream()
          .map(ForkJoinTask::join)
          .flatMap(map -> map.entrySet().stream())
          .forEach(entry -> tempMap.putIfAbsent(entry.getKey(), entry.getValue()));
      return tempMap;
    } else {
      return processing(lines);
    }
  }

  private Collection<ForkJoinWordCount> createSubtask() {
    List<ForkJoinWordCount> dividedTasks = new ArrayList<>();
    dividedTasks.add(new ForkJoinWordCount(Arrays.copyOfRange(lines, 0, lines.length / 2)));
    dividedTasks.add(
        new ForkJoinWordCount(Arrays.copyOfRange(lines, lines.length / 2, lines.length)));
    return dividedTasks;
  }

  private HashMap<String, Integer> processing(String[] lines) {
    for (String line : lines) {
      for (String word : TextLoader.getListOfWords(line)) {
        this.wordCounts.putIfAbsent(word, word.length());
      }
    }
    return this.wordCounts;
  }
}
