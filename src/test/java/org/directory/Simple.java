package test.java.org.directory;

class Simple {
//    public void count_Me()
    private int myField;

    public Simple(int initialValue) {
        this.myField = initialValue;
    }

    public void increaseMyFieldValue(int increment) {
        this.myField += increment;
    }
}
