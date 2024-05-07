package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the FileProcessor interface that processes Java and Kotlin files for code analysis.
 */
public class UniversalFileProcessor implements FileProcessor {

  private static final Pattern JAVA_METHOD_DECLARATION_PATTERN = Pattern.compile("(private|protected|public)\\s+(\\w+)\\s+(\\w+)\\s*\\(.*\\)\\s*\\{");
  private static final Pattern KOTLIN_METHOD_DECLARATION_PATTERN = Pattern.compile("fun\\s+(\\w+)\\s*\\(.*\\)\\s*\\{");

  /**
   * Processes the file for code style analysis and complexity calculation.
   *
   * @param file the file to be processed
   */
  @Override
  public void process(File file) {
    boolean methodProcessing = false;
    int complexity = 0;
    String methodName = null;

    String type = file.getName().endsWith(".java") ? "Java" : "Kotlin";

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

        Matcher methodDeclarationMatcher;
        if (type.equals("Java")) {
          methodDeclarationMatcher = JAVA_METHOD_DECLARATION_PATTERN.matcher(line);
        } else {
          methodDeclarationMatcher = KOTLIN_METHOD_DECLARATION_PATTERN.matcher(line);
        }

        if (methodDeclarationMatcher.matches()) {
          methodName = type.equals("Java") ?
              methodDeclarationMatcher.group(3) : methodDeclarationMatcher.group(1);
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
   * Checks if the given method name is valid.
   *
   * @param methodName the method name to be checked
   * @return true if the method name is valid, false otherwise
   */
  private static boolean isValidMethodName(String methodName) {
    return methodName.matches("[a-z]+[A-Za-z0-9]*");
  }
}
