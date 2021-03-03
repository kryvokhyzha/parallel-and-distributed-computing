package com.assignment.task4;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<List<String>> {
  private final Document document;
  private final List<String> searchedWords;

  DocumentSearchTask(Document document, List<String> searchedWords) {
    super();
    this.document = document;
    this.searchedWords = searchedWords;
  }

  @Override
  protected List<String> compute() {
    return WordCounter.occurrencesCount(document, searchedWords) == 0
        ? null
        : Collections.singletonList(document.documentName);
  }
}
