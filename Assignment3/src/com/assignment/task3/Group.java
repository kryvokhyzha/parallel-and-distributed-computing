package com.assignment.task3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group {
  private final String groupName;
  public HashMap<Integer, List<String>> groupList = new HashMap<>();

  public Group(String groupName, int sizeOfGroup) {
    this.groupName = groupName;
    generateGroupList(sizeOfGroup);
  }

  private void generateGroupList(int sizeOfGroup) {
    for (int i = 0; i < sizeOfGroup; i++) {
      this.groupList.put(i + 1, new ArrayList<>());
    }
  }

  public String getGroupName() {
    return groupName;
  }
}
