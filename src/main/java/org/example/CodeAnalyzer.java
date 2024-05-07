package org.example;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CodeAnalyzer is a class responsible for analyzing Java and Kotlin files within a specified directory.
 * It calculates method complexity, identifies non-conforming method names, and prints analysis results.
 */
public class CodeAnalyzer {

  static Map<String, Integer> complexityMap = new HashMap<>();
  static Map<String, String> methodFileMap = new HashMap<>();
  static int totalMethods = 0;
  static int nonConformingMethods = 0;
  static int numOpenBraces = 0;

  /**
   * Main method for running the code analysis.
   *
   * @param args Command-line arguments (not used).
   * @throws IOException If an I/O error occurs.
   */
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Specify the absolute path to the directory you want to analyze:");
    String directoryPath = scanner.next();

    File directory = new File(directoryPath);
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException("Invalid directory path");
    }

    processFiles(directory, ".java", new UniversalProcessor());
    processFiles(directory, ".kt", new UniversalProcessor());

    printTopComplexMethods();
    printNonConformingPercentage();
  }

  /**
   * Processes files with the specified extension using the given processor.
   *
   * @param directory  The directory containing the files to process.
   * @param extension  The file extension to filter files.
   * @param processor  The processor to handle file processing.
   */
  private static void processFiles(File directory, String extension, FileProcessor processor) {
    File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
    if (files != null) {
      Arrays.stream(files).forEach(processor::process);
    }
  }

  /**
   * Prints the top 3 most complex methods/functions found during analysis.
   */
  private static void printTopComplexMethods() {
    if (complexityMap.isEmpty()){
      System.out.println("\nNo methods were found in the directory.");
    }
    else {
      System.out.println("\nTop 3 most complex methods/functions in the directory:");
      complexityMap.entrySet().stream()
          .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
          .limit(3)
          .forEach(entry -> System.out.println(
              "Method: " + entry.getKey() + ", File: " + methodFileMap.get(entry.getKey())
                  + ", Complexity: " + entry.getValue()));
    }
  }

  /**
   * Prints the percentage of methods not adhering to the naming convention.
   */
  private static void printNonConformingPercentage() {
    if (totalMethods != 0){
      double nonConformingPercentage = (double) nonConformingMethods / totalMethods * 100;
      DecimalFormat df = new DecimalFormat("#.##");
      System.out.println("\nPercentage of methods not adhering to naming convention: "
          + df.format(nonConformingPercentage) + "%");
    }
  }
}
