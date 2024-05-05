import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class CodeAnalyzer {
  static Map<String, Integer> complexityMap = new HashMap<>();
  static Map<String, String> methodFileMap = new HashMap<>();
  static int totalMethods = 0, nonConformingMethods = 0, numOpenBraces = 0;

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: java CodeAnalyzer <directory>");
      return;
    }

    String directoryPath = args[0];
    File directory = new File(directoryPath);
    if (!directory.isDirectory()) {
      System.out.println("Invalid directory path");
      return;
    }

    processFiles(directory, ".java", CodeAnalyzer::processJavaFile);
    processFiles(directory, ".kt", CodeAnalyzer::processKotlinFile);

    printTopComplexMethods();
    printNonConformingPercentage();
  }

  private static void processFiles(File directory, String extension, FileProcessor processor) {
    File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
    if (files != null) {
      Arrays.stream(files).forEach(processor::process);
    }
  }

  private static void printTopComplexMethods() {
    System.out.println("Top 3 most complex methods/functions in the directory:");
    complexityMap.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(3)
        .forEach(entry -> System.out.println(
            "Method: " + entry.getKey() + ", File: " + methodFileMap.get(entry.getKey())
                + ", Complexity: " + entry.getValue()));
  }

  private static void printNonConformingPercentage() {
    double nonConformingPercentage = (double) nonConformingMethods / totalMethods * 100;
    DecimalFormat df = new DecimalFormat("#.##");
    System.out.println("\nPercentage of methods not adhering to naming convention: "
        + df.format(nonConformingPercentage) + "%");
  }

  private interface FileProcessor {
    void process(File file);
  }

  private static void processKotlinFile(File file) {
    processFile(file, "fun", CodeAnalyzer::extractKotlinMethodName);
  }

  private static void processJavaFile(File file) {
    processFile(file, "(private|protected|public)", CodeAnalyzer::extractMethodName);
  }

  private static void processFile(File file, String methodPrefix, MethodNameExtractor extractor) {
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

        if (line.matches(methodPrefix + " .*")) {
          methodName = extractor.extract(line);
          if (methodName != null) {
            methodProcessing = true;
            totalMethods++;
            if (!isValidMethodName(methodName)) {
              nonConformingMethods++;
            }
          }
        }

        if (methodProcessing) {
          if (line.matches(".*(if|else|switch|case|for|while|default).*")) {
            complexity++;
          }
          if (line.contains("{")) {
            numOpenBraces++;
          }
          if (line.contains("}")) {
            if (numOpenBraces == 1) {
              methodProcessing = false;
              complexityMap.put(methodName, complexity);
              methodFileMap.put(methodName, file.getName());
              complexity = 0;
            }
            numOpenBraces--;
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private interface MethodNameExtractor {
    String extract(String line);
  }

  private static String extractKotlinMethodName(String line) {
    line = line.trim();
    int startIndex = line.indexOf(' ') + 1;
    int endIndex = line.indexOf('(');
    return endIndex <= startIndex ? null : line.substring(startIndex, endIndex);
  }

  private static String extractMethodName(String line) {
    line = line.trim();
    int startIndex = line.indexOf(' ') + 1;
    String[] types = {"void", "int", "long", "short", "double", "float", "boolean",
        "byte", "char", "String", "int[]", "long[]", "short[]", "double[]", "float[]", "boolean[]",
        "byte[]", "char[]", "String[]"};
    for (String type : types) {
      if (line.contains(type)) {
        startIndex = line.indexOf(type) + type.length() + 1;
        break;
      }
    }
    int endIndex = line.indexOf('(');
    return endIndex <= startIndex ? null : line.substring(startIndex, endIndex);
  }

  private static boolean isValidMethodName(String methodName) {
    return methodName.matches("[a-z]+[A-Za-z0-9]*");
  }
}
