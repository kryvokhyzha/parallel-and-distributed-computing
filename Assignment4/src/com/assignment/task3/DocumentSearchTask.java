package com.assignment.task3;

import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Long> {
  private final Document document;
  private final String searchedWord;

  DocumentSearchTask(Document document, String searchedWord) {
    super();
    this.document = document;
    this.searchedWord = searchedWord;
  }

  @Override
  protected Long compute() {
    return WordCounter.occurrencesCount(document, searchedWord);
  }
}
