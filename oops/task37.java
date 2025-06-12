public class task37 {
    void add(int x, int y) {
        System.out.println("x = " + x + " y = " + y);
    }

    void add(int x, int y, int z) {
        System.out.println("x = " + x + " y = " + y + " z = " + z);
    }

    public static void main(String[] args) {
        task37 obj = new task37();
        obj.add(10, 20, 30);
        obj.add(50, 100);
    }
}