package com.assignment.task4;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {

  public static String[] wordsIn(String line) {
    return line.trim().split("(\\s|\\p{Punct})+");
  }

  public static Long occurrencesCount(Document document, List<String> searchedWords) {
    long count = 0;
    for (String line : document.getLines()) {
      for (String word : wordsIn(line)) {
        if (searchedWords.contains(word)) {
          count = count + 1;
        }
      }
    }
    return count;
  }

  private final ForkJoinPool forkJoinPool =
      new ForkJoinPool(Runtime.getRuntime().availableProcessors());

  public List<String> countOccurrencesInParallel(Folder folder, List<String> searchedWords) {
    return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWords));
  }

  public Long countOccurrencesOnSingleThread(Folder folder, List<String> searchedWords) {
    long count = 0;
    for (Folder subFolder : folder.getSubFolders()) {
      count = count + countOccurrencesOnSingleThread(subFolder, searchedWords);
    }
    for (Document document : folder.getDocuments()) {
      count = count + occurrencesCount(document, searchedWords);
    }
    return count;
  }
}
