package task03;

public class SingletonDP {
    public static void main(String[] args) {
        SingletonDemo.getInstance().doHere();

        // Following code won't work as constructor is private:
        // SingletonDemo obj = new SingletonDemo();
        // obj.doHere();
        // SingletonDemo obj2 = new SingletonDemo();
        // obj2.doHere();
    }
}