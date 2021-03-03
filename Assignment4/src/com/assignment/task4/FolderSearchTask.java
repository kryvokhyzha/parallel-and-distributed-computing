package com.assignment.task4;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Long> {
  private final Folder folder;
  private final String searchedWord;

  FolderSearchTask(Folder folder, String searchedWord) {
    super();
    this.folder = folder;
    this.searchedWord = searchedWord;
  }

  @Override
  protected Long compute() {
    long count = 0L;
    List<RecursiveTask<Long>> forks = new LinkedList<>();
    for (Folder subFolder : folder.getSubFolders()) {
      FolderSearchTask task = new FolderSearchTask(subFolder, searchedWord);
      forks.add(task);
      task.fork();
    }
    for (Document document : folder.getDocuments()) {
      DocumentSearchTask task = new DocumentSearchTask(document, searchedWord);
      forks.add(task);
      task.fork();
    }
    for (RecursiveTask<Long> task : forks) {
      count = count + task.join();
    }
    return count;
  }
}
