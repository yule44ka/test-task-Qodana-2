package test.java.org.directory;

public class MultipleMethods {
    public void method1() {
        int x = 10;
        if (x < 0) {
            System.out.println("Negative");
        } else {
            System.out.println("Non-negative");
        }
        for (int i = 0; i < 10; i++){
            System.out.println(i);
        }
    }

    public void method2(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] % 2 == 0) {
                System.out.println("Even");
            } else {
                System.out.println("Odd");
            }
        }
    }

    public void method3() {
        int y = 5;
        int z = 3;
        while (z > 0) {
            y *= 2;
            z--;
        }
        System.out.println("Result: " + y);
    }
}
