package org.example.directory;

public class LoopSwitch {
    public void loop_method(int[] array) {
        for (int i = 0; i < array.length; i++) {
            switch (array[i]) {
                case 0:
                    System.out.println("Zero");
                    break;
                case 1:
                    System.out.println("One");
                    break;
                default:
                    System.out.println("Other");
            }
        }
    }
}
