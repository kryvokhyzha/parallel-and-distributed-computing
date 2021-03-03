package com.assignment.task3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {

  public static String[] wordsIn(String line) {
    return line.trim().split("(\\s|\\p{Punct})+");
  }

  public static Set<String> getAllWords(Document document) {
    Set<String> allWords = new HashSet<>();
    for (String line : document.getLines()) {
      allWords.addAll(Arrays.asList(wordsIn(line)));
    }
    return allWords;
  }

  private final ForkJoinPool forkJoinPool =
      new ForkJoinPool(Runtime.getRuntime().availableProcessors());

  public Set<String> getCommonWordsInParallel(Folder folder) {
    return forkJoinPool.invoke(new FolderSearchTask(folder));
  }
}
