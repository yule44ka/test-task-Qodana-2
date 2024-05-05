package main.java.org;

import java.io.File;

/**
 * FileProcessor is an interface for classes that process files.
 */
public interface FileProcessor {

  /**
   * Processes the given file.
   *
   * @param file The file to be processed.
   */
  void process(File file);
}

