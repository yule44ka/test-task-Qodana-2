package org.example.directory;

import java.util.ArrayList;

public class SimpleFunction {

  public static void main(String[] args) {
    int result = add(3, 4);
    System.out.println("Result of addition: " + result);
    ArrayList<String> my_list = names_list("Julia", "Bob");
    System.out.println(my_list);
  }

  public static int add(int a, int b) {
    return a + b;
  }

  public static ArrayList<String> names_list (String a, String b){
    ArrayList<String> names = new ArrayList<>();
    names.add(a);
    names.add(b);
    return names;
  }
}
