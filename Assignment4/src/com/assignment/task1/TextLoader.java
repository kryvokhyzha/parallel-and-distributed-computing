package com.assignment.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TextLoader {
  public static List<String> getListOfWords(String text) {
    return Arrays.asList(text.trim().split("(\\s|\\p{Punct})+"));
  }

  public static List<String> getLinesFromFile(String fileName) throws IOException {
    String[] lineBylineResult;
    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      lineBylineResult = stream.toArray(String[]::new);
    }
    return Arrays.asList(lineBylineResult);
  }

  public static List<String> getWordsFromFile(String fileName) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      stream.forEach(s -> contentBuilder.append(s).append("\n"));
    }
    return TextLoader.getListOfWords(contentBuilder.toString());
  }
}
