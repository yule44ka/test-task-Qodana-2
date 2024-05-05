package main.java.org;

import java.io.*;

/**
 * KotlinFileProcessor is a class that implements the FileProcessor interface to process Kotlin files.
 * It analyzes the complexity of methods within the file and extracts method names.
 */
public class KotlinFileProcessor implements FileProcessor {

  /**
   * Processes the given Kotlin file to analyze method complexity and extract method names.
   * Updates various metrics such as total methods, non-conforming methods, etc.
   *
   * @param file The Kotlin file to be processed.
   */
  @Override
  public void process(File file) {
    boolean methodProcessing = false;
    int complexity = 0;
    String methodName = null;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      boolean inCommentBlock = false;

      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("/*")) {
          inCommentBlock = true;
        }
        if (inCommentBlock) {
          if (line.endsWith("*/")) {
            inCommentBlock = false;
          }
          continue;
        }
        if (line.startsWith("//")) {
          continue;
        }

        if (line.startsWith("fun")) {
          methodName = extractKotlinMethodName(line);
          if (methodName != null) {
            methodProcessing = true;
            CodeAnalyzer.totalMethods++;
            if (!isValidMethodName(methodName)) {
              CodeAnalyzer.nonConformingMethods++;
            }
          }
        }

        if (methodProcessing) {
          if (line.matches(".*(if|else|switch|case|for|while|default).*")) {
            complexity++;
          }
          if (line.contains("{")) {
            CodeAnalyzer.numOpenBraces++;
          }
          if (line.contains("}")) {
            if (CodeAnalyzer.numOpenBraces == 1) {
              methodProcessing = false;
              CodeAnalyzer.complexityMap.put(methodName, complexity);
              CodeAnalyzer.methodFileMap.put(methodName, file.getName());
              complexity = 0;
            }
            CodeAnalyzer.numOpenBraces--;
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Extracts the method name from the given Kotlin function declaration line.
   *
   * @param line The line containing the Kotlin function declaration.
   * @return The extracted method name, or null if not found.
   */
  private static String extractKotlinMethodName(String line) {
    line = line.trim();
    int startIndex = line.indexOf(' ') + 1;
    int endIndex = line.indexOf('(');
    if (endIndex <= startIndex) {
      return null;
    }
    return line.substring(startIndex, endIndex);
  }

  /**
   * Checks if the given method name is valid according to the specified naming convention.
   * Method names should start with a lowercase letter and can contain alphanumeric characters and underscores.
   *
   * @param methodName The method name to be validated.
   * @return true if the method name is valid, false otherwise.
   */
  private static boolean isValidMethodName(String methodName) {
    return methodName.matches("[a-z]+[A-Za-z0-9]*");
  }
}
