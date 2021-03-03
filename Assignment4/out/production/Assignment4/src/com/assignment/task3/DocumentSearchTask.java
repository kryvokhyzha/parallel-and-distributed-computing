package com.assignment.task3;

import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Set<String>> {
  private final Document document;

  DocumentSearchTask(Document document) {
    super();
    this.document = document;
  }

  public Document getDocument() {
    return document;
  }

  @Override
  protected Set<String> compute() {
    return WordCounter.getAllWords(document);
  }
}
