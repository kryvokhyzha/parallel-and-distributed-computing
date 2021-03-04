package com.assignment.task4;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    String folderName = "resources";
    List<String> keyWords = Arrays.asList("java", "scala");

    WordCounter wordCounter = new WordCounter();
    Folder folder = Folder.fromDirectory(new File(folderName));

    for (String documentName : wordCounter.countOccurrencesInParallel(folder, keyWords))
      System.out.println(documentName);
  }
}
