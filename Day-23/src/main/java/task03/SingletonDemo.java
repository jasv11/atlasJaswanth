package task03;

class SingletonDemo {
    private static SingletonDemo instance; // loading ids.. //pass/ pin no
    
    private SingletonDemo() {
        System.out.println("initiating the singleton");
    }

    public static SingletonDemo getInstance() {
        if (instance == null) {
            instance = new SingletonDemo();
            System.out.println("in get instance");
        }
        return instance;
    }
    
    public static void doHere() {
        System.out.println("doing here some thing");
    }
}