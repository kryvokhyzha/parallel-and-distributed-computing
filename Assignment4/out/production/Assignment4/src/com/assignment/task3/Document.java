package com.assignment.task3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Document {
  private final List<String> lines;
  public final String documentName;

  Document(List<String> lines, String documentName) {
    this.lines = lines;
    this.documentName = documentName;
  }

  List<String> getLines() {
    return this.lines;
  }

  static Document fromFile(File file) throws IOException {
    List<String> lines = new LinkedList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = reader.readLine();
      while (line != null) {
        lines.add(line);
        line = reader.readLine();
      }
    }

    return new Document(lines, file.getAbsolutePath());
  }
}
