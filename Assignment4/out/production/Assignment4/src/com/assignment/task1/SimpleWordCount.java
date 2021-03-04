package com.assignment.task1;

import java.util.HashMap;
import java.util.List;

public class SimpleWordCount {
  public static HashMap<String, Integer> processing(List<String> words) {
    HashMap<String, Integer> wordCounts = new HashMap<>();

    for (String word : words) {
      wordCounts.putIfAbsent(word, word.length());
    }

    return wordCounts;
  }
}
