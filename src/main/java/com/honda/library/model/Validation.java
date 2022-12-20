package com.honda.library.model;

public class Validation {
  private final static String illegalName = "1234567890'\\!#@$%^&*()_-+=~`/{}[]";
  private final static String illegalID = "'\\!#@$%^&*()_-+=~`/{}[]";

  private static boolean containsIllegal(String word, String unacceptable) {
    for (int i = 0; i < word.length(); i++)
      if (unacceptable.contains("" + word.charAt(i)))
        return true;
    return false;
  }

  public static boolean validateName(String name) {
    return (!name.isEmpty() && containsIllegal(name, illegalName));
  }
  public static boolean validateID(String id) {
    return (!id.isEmpty() && containsIllegal(id, illegalID));
  }
}
