public class task38 {
    void add(char x, char y) {
        System.out.println("x = " + x + " y = " + y);
    }
    
    void add(int x, int y) {
        System.out.println("x = " + x + " y = " + y);
    }

    void add(int x, float y) {
        System.out.println("x = " + x + " y = " + y);
    }

    void add(float x, int y) {
        System.out.println("x = " + x + " y = " + y);
    }

    public static void main(String[] args) {
        task38 obj = new task38();
        obj.add('d', 'a');
        obj.add(100, 100);
        obj.add(10.50f, 60);
        obj.add(100, 80.80f);
    }
}