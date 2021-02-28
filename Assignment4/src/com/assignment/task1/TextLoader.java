package com.assignment.task1;

public class TextLoader {
  public static String[] getListOfWords(String text) {
    return text.trim().split("(\\s|\\p{Punct})+");
  }
}
