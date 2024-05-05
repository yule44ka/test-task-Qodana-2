# test-task-Qodana-2

## Code Analyzer
### How to run:
1) Run **CodeAnalyzer.java** (*src/main/java/org/CodeAnalyzer.java*).
2) Write the absolute path to the directory you want to analyze.
   I prepared a directory that can be used for testing, it is located in *src/test/java/org*:
   - *directory* - directory with Java and Kotlin files 
   You can also test on the empty directory, it will work correctly.
### Description:
1) **CodeAnalyzer** - a class that is responsible for analyzing Java and Kotlin files and outputting analysis results.
2) *FileProcessor* - an interface that defines the contract for processing files. Classes implementing this interface are responsible for defining the behavior for processing different types of files: Java and Kotlin files.
3) **JavaFileProcessor** and **KotlinFileProcessor** - classes that implement the *FileProcessor* interface for Java and Kotlin files accordingly. They contains methods to extract name of methods or functions, check camelCase codestyle and count number of the conditional statements for each method and function.
### Output:
Specify the absolute path to the directory you want to analyze:
<path>

Top 3 most complex methods/functions in the directory:
Method: <method1>, File: <in what file method1 is located>, Complexity: <complexity of method1>
Method: <method2>, File:  <in what file method2 is located>, Complexity: <complexity of method2>
Method: <method3>, File:  <in what file method3 is located>, Complexity: <complexity of method3>

Percentage of methods not adhering to naming convention: <percent>%
