class OuterClass {
    int x = 10;
    private class InnerClass {
        int y = 5;
    }
    
    // Method to access private inner class
    public int getSum() {
        InnerClass inner = new InnerClass();
        return this.x + inner.y;
    }
}

public class task12 {
    public static void main(String[] args) {
        OuterClass myOuter = new OuterClass();
        System.out.println(myOuter.getSum());
    }
}
