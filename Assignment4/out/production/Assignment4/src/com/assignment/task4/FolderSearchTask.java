package com.assignment.task4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<List<String>> {
  private final Folder folder;
  private final List<String> searchedWords;

  FolderSearchTask(Folder folder, List<String> searchedWords) {
    super();
    this.folder = folder;
    this.searchedWords = searchedWords;
  }

  @Override
  protected List<String> compute() {
    List<RecursiveTask<List<String>>> forks = new LinkedList<>();

    for (Folder subFolder : folder.getSubFolders()) {
      FolderSearchTask task = new FolderSearchTask(subFolder, searchedWords);
      forks.add(task);
      task.fork();
    }
    for (Document document : folder.getDocuments()) {
      DocumentSearchTask task = new DocumentSearchTask(document, searchedWords);
      forks.add(task);
      task.fork();
    }

    List<String> result = new ArrayList<>();
    for (RecursiveTask<List<String>> task : forks) {
      List<String> taskResult = task.join();
      if (taskResult == null) {
        continue;
      }
      result.addAll(taskResult);
    }
    return result;
  }
}
