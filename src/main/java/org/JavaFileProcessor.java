package main.java.org;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaFileProcessor is a class that implements the FileProcessor interface to process Java files.
 * It analyzes the complexity of methods within the file and keeps track of various metrics such as method names, complexity, etc.
 */
public class JavaFileProcessor implements FileProcessor {

  // Regular expression patterns for method declaration and method name extraction
  private static final Pattern METHOD_DECLARATION_PATTERN = Pattern.compile("(private|protected|public)\\s+(\\w+)\\s+(\\w+)\\s*\\(.*\\)\\s*\\{");
  private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("\\b\\w+\\b");

  /**
   * Processes the given Java file to analyze method complexity and extract method names.
   * Updates various metrics such as total methods, non-conforming methods, etc.
   *
   * @param file The Java file to be processed.
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

        Matcher methodDeclarationMatcher = METHOD_DECLARATION_PATTERN.matcher(line);
        if (methodDeclarationMatcher.matches()) {
          methodName = methodDeclarationMatcher.group(3);
          if (methodName != null) {
            methodProcessing = true;
            CodeAnalyzer.totalMethods++;
            if (!isValidMethodName(methodName)) {
              CodeAnalyzer.nonConformingMethods++;
            }
          }
        }

        if (methodProcessing) {
          Matcher methodNameMatcher = METHOD_NAME_PATTERN.matcher(line);
          while (methodNameMatcher.find()) {
            String word = methodNameMatcher.group();
            if (word.equals("if") || word.equals("else") || word.equals("switch") ||
                word.equals("case") || word.equals("for") || word.equals("while") ||
                word.equals("default")) {
              complexity++;
            }
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
